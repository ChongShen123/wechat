package com.xsdkj.wechat.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xsdkj.wechat.constant.RabbitConstant.FANOUT_CHAT_NAME;
import static com.xsdkj.wechat.constant.RabbitConstant.FANOUT_SERVICE_NAME;


/**
 * RabbitMQ 分列模式
 *
 * @author tiankong
 * @date 2019/12/26 12:59
 */
@Configuration
public class RabbitFanoutConfig {
    /**
     * 聊天交换器
     *
     * @return FanoutExchange
     */
    @Bean
    FanoutExchange chatFanoutExchange() {
        return new FanoutExchange(FANOUT_CHAT_NAME, true, false);
    }

    /**
     * 服务交换器
     *
     * @return FanoutExchange
     */
    @Bean
    FanoutExchange serviceFanoutExchange() {
        return new FanoutExchange(FANOUT_SERVICE_NAME, true, false);
    }

    /**
     * 聊天主队列
     *
     * @return Queue
     */
//    @Bean
//    Queue chatQueueMain() {
//        return new Queue(SystemConstant.CHAT_QUEUE_MAIN);
//    }

    /**
     * 聊天辅队列
     *
     * @return Queue
     */
//    @Bean
//    Queue chatQueueAssist() {
//        return new Queue(SystemConstant.CHAT_QUEUE_ASSIST);
//    }

    /**
     * 服务主队列
     *
     * @return Queue
     */
//    @Bean
//    Queue serviceQueueMain() {
//        return new Queue(SystemConstant.SERVICE_QUEUE_MAIN);
//    }

    /**
     * 服务辅队列
     *
     * @return Queue
     */
//    @Bean
//    Queue serviceQueueAssist() {
//        return new Queue(SystemConstant.SERVICE_QUEUE_ASSIST);
//    }

    /**
     * 聊天主队列绑定到聊天交换器
     *
     * @return Binding
     */
//    @Bean
//    Binding bindingChatMain() {
//        return BindingBuilder.bind(chatQueueMain()).to(chatFanoutExchange());
//    }

    /**
     * 聊天辅队列绑定到聊天交换器
     *
     * @return Binding
     */
//    @Bean
//    Binding bindingChatAssist() {
//        return BindingBuilder.bind(chatQueueAssist()).to(chatFanoutExchange());
//    }


    /**
     * 服务主队列绑定到服务交换器
     *
     * @return Binding
     */
//    @Bean
//    Binding bindingServiceMain() {
//        return BindingBuilder.bind(serviceQueueMain()).to(serviceFanoutExchange());
//    }

    /**
     * 服务辅队列绑定到服务交换器
     *
     * @return Binding
     */
//    @Bean
//    Binding bindingServiceAssist() {
//        return BindingBuilder.bind(serviceQueueAssist()).to(serviceFanoutExchange());
//    }
}
