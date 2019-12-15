package com.cxkj.wechat.netty.executor.single;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.RequestParamBo;
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

/**
 * @author tiankong
 * @date 2019/12/13 11:34
 */
@Service
@ExecutorAnno(command = Command.SINGLE_CHAT)
public class SingleChatExecutor extends ChatExecutor {

    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        Session session = SessionUtil.getSession(channel);
        SingleChat chat = createNewSingleChat(param.getToUserId(), session.getUserId(), param.getContent(), param.getType());
        // 获取一条消息
        Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(param.getUserId());
        if (toUserChannel != null) {
            chat.setRead(true);
            sendMessage(toUserChannel, JsonResult.success(chat, command));
        } else {
            chat.setRead(false);
        }
        // 放入RabbitMQ
        rabbitTemplateService.addSingleChat(SystemConstant.SINGLE_CHAT_QUEUE_ONE, chat);
    }
}
