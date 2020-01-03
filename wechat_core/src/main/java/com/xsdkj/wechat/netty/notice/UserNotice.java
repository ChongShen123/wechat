package com.xsdkj.wechat.netty.notice;

import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.netty.cmd.base.BaseHandler;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 10:23
 */
@Component
@Slf4j
public class UserNotice extends BaseHandler {
    @Resource
    private RabbitTemplateService rabbitTemplateService;

    @RabbitListener(queues = RabbitConstant.USER_NOTICE_QUEUE)
    public void userNoticeHandler(MsgBox box) {
        switch (box.getType()) {
            case RabbitConstant.USER_PRICE_OPERATION_NOTICE:
                SingleChat singleChat = (SingleChat) box.getData();
                handleUserPriceOperation(singleChat);
                break;
            default:
                log.error("通知信息错误>>>{}", box);
        }
    }

    /**
     * 处理用户充值提现通知
     *
     * @param singleChat singleChat
     */
    private void handleUserPriceOperation(SingleChat singleChat) {
        Integer toUserId = singleChat.getToUserId();
        Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(toUserId);
        if (userChannel != null) {
            sendMessage(userChannel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT));
            singleChat.setRead(true);
        } else {
            singleChat.setRead(false);
        }
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_CHAT_NAME, MsgBox.create(RabbitConstant.BOX_TYPE_SINGLE_CHAT, singleChat));
    }
}
