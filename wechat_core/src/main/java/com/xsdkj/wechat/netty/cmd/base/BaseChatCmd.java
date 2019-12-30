package com.xsdkj.wechat.netty.cmd.base;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.bo.RequestParamBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.entity.chat.Group;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.*;
import com.xsdkj.wechat.service.ex.*;
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

import static com.xsdkj.wechat.common.SystemConstant.KEY_GROUP_ID;
import static com.xsdkj.wechat.common.SystemConstant.KEY_USER_ID;


/**
 * 聊天相关业务 命令抽象类
 *
 * @author tiankong
 * @date 2019/11/17 19:54
 */
@Service
public abstract class BaseChatCmd extends AbstractCmd {
    @Resource
    protected UserService userService;
    @Resource
    protected Snowflake snowflake;
    @Resource
    protected GroupService groupService;
    @Resource
    protected RabbitTemplateService rabbitTemplateService;
    @Resource
    protected FriendApplicationService friendApplicationService;
    @Resource
    protected FriendService friendService;
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
            } catch (ValidateException | ParseParamException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, cmd));
            } catch (DataEmptyException | UserNotFountException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, cmd));
            } catch (DataIntegrityViolationException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_FOND, cmd));
            } catch (GroupNotFoundException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.GROUP_NOT_FOUND, cmd));
            } catch (UserJoinedException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_JOINED_EXCEPTION, cmd));
            } catch (UserNotInGroupException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_IN_GROUP, cmd));
            } catch (PermissionDeniedException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.FORBIDDEN, cmd));
            } catch (NoSayException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.NO_SAY_EXCEPTION, cmd));
            } catch (RepetitionException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.REPEAT_EXCEPTION, cmd));
            } catch (UnAuthorizedException e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.UNAUTHORIZED, cmd));
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
     * 创建一条单聊消息
     *
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     * @param content    content
     * @param type       0信息 1语音 2图片 3撤销 4 加入群聊 5退群 6红包 7转账
     * @return SingleChat
     */
    protected SingleChat createNewSingleChat(Integer toUserId, Integer fromUserId, String content, Byte type) {
        SingleChat chat = new SingleChat();
        chat.setId(snowflake.nextIdStr());
        chat.setContent(content);
        chat.setFromUserId(fromUserId);
        chat.setToUserId(toUserId);
        chat.setType(type);
        chat.setCreateTimes(System.currentTimeMillis());
        return chat;
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
        application.setState(SystemConstant.UNTREATED);
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
    protected void sendCreateGroupMessageToUsers(Set<Integer> ids, Group group) {
        List<SingleChat> list = new ArrayList<>();
        ids.forEach(id -> list.add(createNewSingleChat(id, SystemConstant.SYSTEM_USER_ID, "您已加入【" + group.getName() + "】 开始聊天吧", SystemConstant.JOIN_GROUP)));
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
            rabbitTemplateService.addExchange(SystemConstant.FANOUT_CHAT_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_SINGLE_CHAT, singleChat));
        });
    }

    /**
     * 解析用户Ids和群Id
     *
     * @param param 参数
     */
    protected void parseIdsAndGroupId(JSONObject param) {
        try {
            Set<Integer> ids = Arrays.stream(StrUtil.splitToInt(param.getString(SystemConstant.KEY_IDS), ",")).boxed().collect(Collectors.toSet());
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

    protected void parseUserId(JSONObject param) {
        Integer uid = param.getInteger(KEY_USER_ID);
        if (ObjectUtil.isEmpty(uid)) {
            throw new ValidateException();
        }
        requestParam.setUserId(uid);
    }

    protected void parseTimes(JSONObject param) {
        Long times = param.getLong(SystemConstant.KEY_TIMES);
        if (ObjectUtil.isEmpty(times)) {
            throw new ValidateException();
        }
        requestParam.setTimes(times);
    }

    protected void parseIntegerType(JSONObject param) {
        Integer type = param.getInteger(SystemConstant.KEY_TYPE);
        if (ObjectUtil.isEmpty(type)) {
            throw new ValidateException();
        }
        requestParam.setIntType(type);
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

}
