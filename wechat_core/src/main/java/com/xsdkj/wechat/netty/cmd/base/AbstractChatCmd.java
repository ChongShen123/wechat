package com.xsdkj.wechat.netty.cmd.base;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.bo.RequestParamBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.service.*;
import com.xsdkj.wechat.ex.*;
import com.xsdkj.wechat.util.ChatUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.util.ThreadUtil;
import io.netty.channel.Channel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xsdkj.wechat.constant.ParamConstant.KEY_GROUP_ID;
import static com.xsdkj.wechat.constant.ParamConstant.KEY_USER_ID;

/**
 * 聊天相关业务 命令抽象类
 *
 * @author tiankong
 * @date 2019/11/17 19:54
 */
@Service
public abstract class AbstractChatCmd extends AbstractCmd {
    @Resource
    protected UserService userService;
    @Resource
    protected Snowflake snowflake;
    @Resource
    protected UserGroupService groupService;
    @Resource
    protected RabbitTemplateService rabbitTemplateService;
    @Resource
    protected FriendApplicationService friendApplicationService;
    @Resource
    protected FriendService friendService;
    @Resource
    protected ChatUtil chatUtil;
    @Resource
    protected SingleChatService singleChatService;
    protected RequestParamBo requestParam = new RequestParamBo();
    protected SessionBo session;

    /**
     * 解析参数
     *
     * @param param 参数
     * @throws Exception 异常
     */
    protected abstract void parseParam(JSONObject param) throws Exception;

    /**
     * 具体执行
     *
     * @param channel 用户channel
     * @throws RuntimeException 业务异常
     */
    protected abstract void concreteAction(Channel channel) throws RuntimeException;

    /**
     * 执行命令模板方法
     *
     * @param param   参数
     * @param channel 用户channel
     */
    @Override
    public void execute(JSONObject param, Channel channel) {
        ThreadUtil.getSingleton().submit(() -> {
            try {
                // 获取用户Session
                session = SessionUtil.getSession(channel);
                // 解析参数
                parseParam(param);
                //具体执行
                concreteAction(channel);
            } catch (UserAlreadyRegister e) {
                sendMessage(channel, JsonResult.failed(e.getCode(), cmd));
                remove(channel);
            } catch (ServiceException e) {
                sendMessage(channel, JsonResult.failed(e.getCode(), cmd));
            } catch (DataIntegrityViolationException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_FOND, cmd));
            } catch (NullPointerException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, cmd));
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                sendMessage(channel, JsonResult.failed(cmd));
            }
        });
    }


    /**
     * 获取一条好友申请或回复 消息
     *
     * @param session 发送方
     * @param fid     接收方
     * @param message 消息
     * @param type    0 添加好友 1 回复好友
     * @return 一条消息
     */
    protected FriendApplication createFriendApplication(SessionBo session, Integer fid, String message, byte type) {
        FriendApplication application = new FriendApplication();
        application.setId(snowflake.nextIdStr());
        application.setFromUserId(session.getUid());
        application.setFromUsername(session.getUsername());
        application.setFromUserIcon(session.getIcon());
        application.setToUserId(fid);
        application.setMessage(message);
        application.setState(ChatConstant.UNTREATED);
        application.setRead(false);
        application.setType(type);
        long createTimes = System.currentTimeMillis();
        application.setCreateTimes(createTimes);
        application.setModifiedTime(createTimes);
        return application;
    }

    /**
     * 给用户发送一个入群消息,保存到数据库
     *
     * @param ids   用户ID
     * @param group 群信息
     */
    protected void sendCreateGroupMessageToUsers(Set<Integer> ids, UserGroup group) {
        List<SingleChat> list = new ArrayList<>();
        ids.forEach(id -> list.add(chatUtil.createNewSingleChat(id, SystemConstant.SYSTEM_USER_ID, "您已加入【" + group.getName() + "】 开始聊天吧", ChatConstant.JOIN_GROUP)));
        list.forEach(singleChat -> {
            Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(singleChat.getToUserId());
            if (toUserChannel != null) {
                sendMessage(toUserChannel, JsonResult.success(singleChat));
                // 通知群里在线的用户XXX已经进入群聊
                sendGroupMessage(group.getId(), JsonResult.success(String.format("%s已加入群聊", SessionUtil.getSession(toUserChannel).getUsername()), cmd));
                // 更新用户redis缓存
                userService.updateRedisDataByUid(singleChat.getToUserId());
            }
            singleChat.setRead(toUserChannel != null);
            rabbitTemplateService.addExchange(RabbitConstant.FANOUT_CHAT_NAME, MsgBox.create(RabbitConstant.BOX_TYPE_SINGLE_CHAT, singleChat));
        });
    }

    /**
     * 解析用户Ids和群Id
     *
     * @param param 参数
     */
    protected void parseIdsAndGroupId(JSONObject param) {
        try {
            Set<Integer> ids = Arrays.stream(StrUtil.splitToInt(param.getString(ParamConstant.KEY_IDS), ",")).boxed().collect(Collectors.toSet());
            Integer groupId = param.getInteger(KEY_GROUP_ID);
            if (ObjectUtil.isNotEmpty(groupId)) {
                requestParam.setGroupId(groupId);
            }
            requestParam.setIds(ids);
        } catch (Exception e) {
            throw new ValidateException();
        }
    }

    /**
     * 解析群ID
     *
     * @param param 参数
     */
    protected void parseGroupId(JSONObject param) {
        Integer groupId = param.getInteger(KEY_GROUP_ID);
        if (ObjectUtil.isEmpty(groupId)) {
            throw new ValidateException();
        }
        requestParam.setGroupId(groupId);
    }

    /**
     * 解析用户id
     *
     * @param param 参数
     */
    protected void parseUserId(JSONObject param) {
        Integer uid = param.getInteger(KEY_USER_ID);
        if (ObjectUtil.isEmpty(uid)) {
            throw new ValidateException();
        }
        requestParam.setUserId(uid);
    }

    /**
     * 解析时间
     *
     * @param param 参数
     */
    protected void parseTimes(JSONObject param) {
        Long times = param.getLong(ParamConstant.KEY_TIMES);
        if (ObjectUtil.isEmpty(times)) {
            throw new ValidateException();
        }
        requestParam.setTimes(times);
    }

    /**
     * 解析为Integer类型
     *
     * @param param 参数
     */
    protected void parseIntegerType(JSONObject param) {
        Integer type = param.getInteger(ParamConstant.KEY_TYPE);
        if (ObjectUtil.isEmpty(type)) {
            throw new ValidateException();
        }
        requestParam.setIntType(type);
    }

    /**
     * 解析参数
     *
     * @param param 参数
     * @param type  类型
     */
    protected String parseParam(JSONObject param, String type) {
        String data = param.getString(type);
        if (StrUtil.isBlank(data)) {
            throw new ValidateException();
        }
        return data;
    }

    /**
     * 检查用户是否已入群
     *
     * @param ids     用户id
     * @param groupId 群id
     * @return boolean
     */
    protected boolean checkGroupUserJoined(Set<Integer> ids, Integer groupId) {
        return groupService.checkUserJoined(ids, groupId) >= 1;
    }

    /**
     * 检查用户是否为群管理员
     *
     * @param groupId 群id
     * @param uid     用户id
     * @return boolean
     */
    protected boolean checkAdmin(Integer groupId, Integer uid) {
        List<Integer> adminIds = groupService.listGroupManagerByUserId(groupId);
        return adminIds.contains(uid);
    }
}
