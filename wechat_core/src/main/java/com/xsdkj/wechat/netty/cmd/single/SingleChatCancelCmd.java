package com.xsdkj.wechat.netty.cmd.single;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.SingleChatCancelVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 单聊消息撤销
 *
 * @author tiankong
 * @date 2019/12/13 16:17
 */
@Service
@CmdAnno(cmd = Cmd.SINGLE_CHAT_CANCEL)
public class SingleChatCancelCmd extends BaseChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        try {
            Integer toUserId = param.getInteger(SystemConstant.KEY_TO_USER_ID);
            String singleChatId = param.getString(SystemConstant.KEY_ID);
            requestParam.setToUserId(toUserId);
            requestParam.setId(singleChatId);
        } catch (Exception e) {
            throw new ValidateException();
        }

    }

    @Override
    protected void concreteAction(Channel channel) {
        Channel userChannel = SessionUtil.getUserChannel(requestParam.getToUserId());
        SingleChat singleChat = singleChatService.getById(requestParam.getId());
        if (singleChat != null && System.currentTimeMillis() < singleChat.getCreateTimes() + SystemConstant.CHAT_CANCEL_TIMES) {
            if (userChannel != null) {
                sendMessage(userChannel, JsonResult.success(new SingleChatCancelVo(requestParam.getId()), cmd));
            }
            singleChatService.deleteById(requestParam.getId());
            sendMessage(channel, JsonResult.success(cmd));
        } else {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.UNDO_TIME_EXCEEDED));
        }
    }
}

