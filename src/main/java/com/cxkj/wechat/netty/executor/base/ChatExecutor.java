package com.cxkj.wechat.netty.executor.base;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.netty.ex.DataEmptyException;
import com.cxkj.wechat.netty.ex.ValidateException;
import com.cxkj.wechat.netty.ex.ParseParamException;
import com.cxkj.wechat.service.*;
import com.cxkj.wechat.util.JsonResult;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 命令执行者抽象类
 *
 * @author tiankong
 * @date 2019/11/17 19:54
 */
@Service
public abstract class ChatExecutor extends Executor {
    @Resource
    protected RequestParamFactory requestParamFactory;
    @Resource
    protected UserService userService;
    @Resource
    private Snowflake snowflake;
    @Resource
    protected GroupService groupService;
    @Resource
    protected RabbitTemplateService rabbitTemplateService;
    @Resource
    protected FriendApplicationService friendApplicationService;
    @Resource
    protected FriendService friendService;

    /**
     * 具体执行
     *
     * @param param   参数
     * @param channel 用户Channel
     */
    protected abstract void concreteAction(RequestParamBo param, Channel channel);

    /**
     * 执行命令
     *
     * @param param   参数
     * @param channel 用户channel
     */
    @Override
    public void execute(JSONObject param, Channel channel) {
        try {
            // 解析参数
            RequestParamBo requestParamBo = requestParamFactory.getParam(command, param);
            //具体执行
            concreteAction(requestParamBo, channel);
        } catch (ValidateException e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, command));
        } catch (DataEmptyException e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, command));
        } catch (ParseParamException e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.PARSE_PARAM, command));
        }
    }

    /**
     * 创建一条单聊消息
     *
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     * @param content    content
     * @param type       type
     * @return SingleChat
     */
    protected SingleChat createNewSingleChat(Integer toUserId, Integer fromUserId, String content, Integer type) {
        SingleChat chat = new SingleChat();
        chat.setId(snowflake.nextIdStr());
        chat.setContent(content);
        chat.setFromUserId(fromUserId);
        chat.setToUserId(toUserId);
        chat.setType(SystemConstant.JOIN_GROUP);
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
    protected FriendApplication getFriendApplication(Session session, Integer fid, String message, byte type) {
        FriendApplication application = new FriendApplication();
        application.setFromUserId(session.getUserId());
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

}
