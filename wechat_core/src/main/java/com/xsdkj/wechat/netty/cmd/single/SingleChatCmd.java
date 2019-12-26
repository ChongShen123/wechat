package com.xsdkj.wechat.netty.cmd.single;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.netty.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/13 11:34
 */
@Service
@CmdAnno(cmd = Cmd.SINGLE_CHAT)
public class SingleChatCmd extends BaseChatCmd {

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
        SingleChat chat = createNewSingleChat(requestParam.getToUserId(), session.getUid(), requestParam.getContent(), requestParam.getType());
        // 获取一条消息
        Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(requestParam.getToUserId());
        if (toUserChannel != null) {
            chat.setRead(true);
            sendMessage(toUserChannel, JsonResult.success(chat, cmd));
        } else {
            chat.setRead(false);
        }
        // TODO 使用到RabbitMQ
        rabbitTemplateService.addChatInfo(SystemConstant.FANOUT_CHAT_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_SINGLE_CHAT, chat));
        sendMessage(channel, JsonResult.success(cmd));
    }
}
