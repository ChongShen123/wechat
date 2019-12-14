package com.cxkj.wechat.component;

import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.SingleChat;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * BabbitMQ 消费者
 *
 * @author tiankong
 * @date 2019/12/10 13:26
 */
@Component
public class RabbitReceiver {
    /**
     * 通过@RabbitListener注解指定一个方法是一个消费方法，方法参数就是所接收的消息。
     */
    @RabbitListener(queues = SystemConstant.SINGLE_CHAT_QUEUE_ONE)
    public void handler1(SingleChat singleChat) {
        System.out.println("handler1:" + singleChat);
    }

    @RabbitListener(queues = SystemConstant.SINGLE_CHAT_QUEUE_ONE)
    public void handler2(SingleChat singleChat) {
        System.out.println("handler2:" + singleChat);
    }
}
