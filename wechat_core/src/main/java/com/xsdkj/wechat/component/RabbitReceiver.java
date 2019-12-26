package com.xsdkj.wechat.component;


import com.xsdkj.wechat.bo.RabbitMessageBox;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.GroupChatService;
import com.xsdkj.wechat.service.SingleChatService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RabbitReceiver {
    @Resource
    SingleChatService singleChatService;
    @Resource
    GroupChatService groupChatService;

    /**
     * 通过@RabbitListener注解指定一个方法是一个消费方法，方法参数就是所接收的消息。
     */
    @RabbitListener(queues = SystemConstant.CHAT_QUEUE_MAIN)
    public void handleChatQueueMain(RabbitMessageBox box) {

        System.out.println("聊天主消费者:" + box.getData().toString());
        action(box);
    }


    @RabbitListener(queues = SystemConstant.CHAT_QUEUE_ASSIST)
    public void handleChatQueueAssist(RabbitMessageBox box) {
        System.out.println("聊天辅消费者:" + box.getData().toString());
//        action(box);
    }

    @RabbitListener(queues = SystemConstant.CHAT_QUEUE_ASSIST)
    public void handleServiceQueueMain(RabbitMessageBox box) {
        System.out.println("服务主消费者:" + box.getData().toString());
//        action(box);
    }

    @RabbitListener(queues = SystemConstant.CHAT_QUEUE_ASSIST)
    public void handleServiceQueueAssist(RabbitMessageBox box) {
        System.out.println("服务辅消费者:" + box.getData().toString());
//        action(box);
    }

    private void action(RabbitMessageBox box) {
        switch (box.getType()) {
            case SystemConstant.BOX_TYPE_SING_CHAT:
                // 单聊
                SingleChat singleChat = (SingleChat) box.getData();
                System.out.println("chatQueueMain:" + singleChat);
                singleChatService.save(singleChat);
                break;
            case SystemConstant.BOX_TYPE_GROUP_CHAT:
                GroupChat groupChat = (GroupChat) box.getData();
                //群聊
                groupChatService.save(groupChat);
                System.out.println("群聊:" + groupChat);
                break;
            default:
                log.error("RabbitMQ传输类型错误:{}", box.getType());
        }
    }
}
