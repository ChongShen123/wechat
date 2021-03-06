package com.cxkj.wechat.component;

import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.ChatInfo;
import com.cxkj.wechat.entity.GroupChat;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.service.GroupChatService;
import com.cxkj.wechat.service.SingleChatService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * BabbitMQ 消费者
 *
 * @author tiankong
 * @date 2019/12/10 13:26
 */

@Component
public class RabbitReceiver {
    @Resource
    SingleChatService singleChatService;
    @Resource
    GroupChatService groupChatService;

    /**
     * 通过@RabbitListener注解指定一个方法是一个消费方法，方法参数就是所接收的消息。
     */
    @RabbitListener(queues = SystemConstant.SINGLE_CHAT_QUEUE_ONE)
    public void handler1(SingleChat singleChat ) {
        System.out.println("handler1:" + singleChat);
        //单聊
        singleChatService.save(singleChat);


    }

    @RabbitListener(queues = SystemConstant.GROUP_CHAT_QUEUE_ONE)
    public void handlerGroupChat(GroupChat groupChat) {
        //群聊
        groupChatService.save(groupChat);
        System.out.println("群聊:" + groupChat);
    }
}
