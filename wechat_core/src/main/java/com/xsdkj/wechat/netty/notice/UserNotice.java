package com.xsdkj.wechat.netty.notice;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.mood.UserMood;
import com.xsdkj.wechat.netty.cmd.base.BaseHandler;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.SingleChatService;
import com.xsdkj.wechat.service.UserMoodService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户推送相关操作
 *
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
    }


}
