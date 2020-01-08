package com.xsdkj.wechat.netty.notice;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.netty.cmd.base.BaseHandler;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.SingleChatService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户推送相关操作
 * @author tiankong
 * @date 2020/1/3 10:23
 */
@Slf4j
@Component
public class UserNotice extends BaseHandler {
    @Resource
    private SingleChatService singleChatService;

    @RabbitListener(queues = RabbitConstant.USER_NOTICE_QUEUE)
    public void userNoticeHandler(MsgBox box) {
        long begin = System.currentTimeMillis();
        log.debug("判断消息类型");
        switch (box.getType()) {
            case RabbitConstant.USER_PRICE_OPERATION_NOTICE:
                SingleChat singleChat = (SingleChat) box.getData();
                handleUserPriceOperation(singleChat, begin);
                break;
            default:
                log.error("通知信息错误>>>{}", box);
        }
    }

    /**
     * 处理用户充值提现通知
     *
     * @param singleChat singleChat
     * @param begin
     */
    private void handleUserPriceOperation(SingleChat singleChat, long begin) {
        log.debug("开始处理金额操作通知...");
        Integer toUserId = singleChat.getToUserId();
        log.debug("被通知用户id:{} {}ms", toUserId, DateUtil.spendMs(begin));
        Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(toUserId);
        if (userChannel != null) {
            sendMessage(userChannel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT));
            log.debug("已将通知信息发送给该用户 {}ms", DateUtil.spendMs(begin));
            singleChat.setRead(true);
            singleChatService.save(singleChat);
            return;
        }
        singleChat.setRead(false);
        singleChatService.save(singleChat);
        log.debug("该用户不在线,已将通知消息离线保存. {}ms", DateUtil.spendMs(begin));
        log.debug("金额通知完成. 用时{}ms", DateUtil.spendMs(begin));
    }
}
