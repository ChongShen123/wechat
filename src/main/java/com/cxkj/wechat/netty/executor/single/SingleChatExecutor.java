package com.cxkj.wechat.netty.executor.single;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/13 11:34
 */
@Service
@ExecutorAnno(command = Command.SINGLE_CHAT)
public class SingleChatExecutor extends ChatExecutor {

    @Resource
    private Snowflake snowflake;

    @Override
    public void execute(JSONObject param, Channel channel) {
        Integer toUserId;
        String content;
        Byte type;
        try {
            toUserId = param.getInteger(SystemConstant.KEY_TO_USER_ID);
            content = param.getString(SystemConstant.KEY_CONTENT);
            type = param.getByte(SystemConstant.KEY_TYPE);
            if (ObjectUtil.isEmpty(toUserId) || ObjectUtil.isEmpty(content) || ObjectUtil.isEmpty(type)) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, command));
            return;
        }
        Session session = SessionUtil.getSession(channel);
        SingleChat chat = createNewSingleChat(toUserId, session.getUserId(), content, type);
        // 获取一条消息
        Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(toUserId);
        if (toUserChannel != null) {
            chat.setRead(true);
            sendMessage(toUserChannel, JsonResult.success(chat, command));
        } else {
            chat.setRead(false);
        }
        // 放入RabbitMQ
        rabbitTemplateService.addSingleChat(SystemConstant.SINGLE_CHAT_QUEUE_ONE, chat);
    }

    private SingleChat createNewSingleChat(Integer toUserId, Integer fromUserId, String content, Byte type) {
        SingleChat chat = new SingleChat();
        chat.setId(snowflake.nextIdStr());
        chat.setContent(content);
        chat.setFromUserId(fromUserId);
        chat.setToUserId(toUserId);
        chat.setType(type);
        chat.setCreateTimes(System.currentTimeMillis());
        return chat;
    }
}
