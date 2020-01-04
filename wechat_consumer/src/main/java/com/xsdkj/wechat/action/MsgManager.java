package com.xsdkj.wechat.action;


import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 消息存储管理类
 *
 * @author tiankong
 * @date 2019/12/10 13:26
 */
@Component
@Slf4j
public class MsgManager implements ApplicationListener<ContextRefreshedEvent> {
    private static Map<Integer, MsgHandler> messageManagerMap = new HashMap<>();
    private int a = 0;
    private int b = 0;

    /**
     * 通过@RabbitListener注解指定一个方法是一个消费方法，方法参数就是所接收的消息。
     */
    @RabbitListener(queues = RabbitConstant.CHAT_QUEUE_MAIN)
    public void handleChatQueueMain(MsgBox box) {
        log.info("a:{}", a++);
        action(box);
    }

    @RabbitListener(queues = RabbitConstant.CHAT_QUEUE_ASSIST)
    public void handleChatQueueAssist(MsgBox box) {
        log.info("b:{}", b++);
//        action(box);
    }

    @RabbitListener(queues = RabbitConstant.SERVICE_QUEUE_MAIN)
    public void handleServiceQueueMain(MsgBox box) {
        log.info("main");
        action(box);
    }

    @RabbitListener(queues = RabbitConstant.SERVICE_QUEUE_ASSIST)
    public void handleServiceQueueAssist(MsgBox box) {
        log.info("assist");
//        action(box);
    }

    private void action(MsgBox box) {
        MsgHandler messageHandler = messageManagerMap.get(box.getType());
        if (messageHandler == null) {
            log.error("信息为空");
            return;
        }
        messageHandler.execute(box);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(SaveAnno.class);
        beans.forEach((name, bean) -> {
            try {
                SaveAnno annotation = bean.getClass().getAnnotation(SaveAnno.class);
                messageManagerMap.put(annotation.type(), (MsgHandler) bean);
            } catch (Exception e) {
                log.error(String.valueOf(e));
            }
        });
    }
}
