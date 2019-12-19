package com.cxkj.wechat.netty.executor.single;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.netty.ex.ValidateException;
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
    protected void parseParam(JSONObject param) {
        Integer toUserId = param.getInteger(SystemConstant.KEY_TO_USER_ID);
        String content = param.getString(SystemConstant.KEY_CONTENT);
        Byte type = param.getByte(SystemConstant.KEY_TYPE);
        if (ObjectUtil.isEmpty(toUserId) || ObjectUtil.isEmpty(content) || ObjectUtil.isEmpty(type)) {
            throw new ValidateException();
        }
        requestParam.setToUserId(toUserId);
        requestParam.setContent(content);
        requestParam.setType(type);
    }

    @Override
    protected void concreteAction(Channel channel) {
        SingleChat chat = createNewSingleChat(requestParam.getToUserId(), session.getUserId(), requestParam.getContent(), requestParam.getType());
        // 获取一条消息
        Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(requestParam.getToUserId());
        if (toUserChannel != null) {
            chat.setRead(true);
            sendMessage(toUserChannel, JsonResult.success(chat, command));
        } else {
            chat.setRead(false);
        }
        // 放入RabbitMQ
        rabbitTemplateService.addChatInfo(SystemConstant.SINGLE_CHAT_QUEUE_ONE, chat);
    }
}
