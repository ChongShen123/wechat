package com.xsdkj.wechat.config;

import com.xsdkj.wechat.common.SystemConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * 消息队列 direct模式配置文件
 * 未采用
 *
 * @author tiankong
 * @date 2019/12/10 13:17
 */
//@Configuration
@Deprecated
public class RabbitDirectConfig {
    /**
     * 交换器名字
     */
    public final static String DIRECT_NAME = SystemConstant.DIRECT_NAME;

    /**
     * 创建一个消息队列
     * 单聊队列1
     */
    @Bean
    Queue singleChatQueueOne() {
        return new Queue(SystemConstant.CHAT_QUEUE_MAIN);
    }

    @Bean
    Queue singleChatQueueTow() {
        return new Queue(SystemConstant.CHAT_QUEUE_ASSIST);
    }

    @Bean
    Queue groupChatQueueOne() {
        return new Queue(SystemConstant.GROUP_CHAT_QUEUE_MAIN);
    }

    @Bean
    Queue groupChatQueueTow() {
        return new Queue(SystemConstant.GROUP_CHAT_QUEUE_ASSIST);
    }

    /**
     * 创建一个交换器
     * 三个参数的说明：
     * 名字，重启后是否依然有效，长期未用是否删除
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DIRECT_NAME, true, false);
    }

    /**
     * DirectExchange 和 Binding 两个Bean的配置可以活力掉，即如果使用DirectExchange，只配置一个Queue的实例即可。
     */
    @Bean
    Binding bindingSingleChatOne() {
        return BindingBuilder.bind(singleChatQueueOne()).to(directExchange()).with(SystemConstant.DIRECT_PATTERN);
    }

    @Bean
    Binding bindingSingleChatTow() {
        return BindingBuilder.bind(singleChatQueueTow()).to(directExchange()).with(SystemConstant.DIRECT_PATTERN);
    }

    @Bean
    Binding bindingGroupChatOne() {
        return BindingBuilder.bind(groupChatQueueOne()).to(directExchange()).with(SystemConstant.DIRECT_PATTERN);
    }

    @Bean
    Binding bindingGroupChatTow() {
        return BindingBuilder.bind(groupChatQueueTow()).to(directExchange()).with(SystemConstant.DIRECT_PATTERN);
    }
}
