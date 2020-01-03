package com.xsdkj.wechat.constant;

/**
 * @author tiankong
 * @date 2019/12/30 12:07
 */
public interface RabbitConstant {
    /**
     * 直连模式交换器名字
     */
    String DIRECT_NAME = "chat-direct";
    /**
     * fanout 分列模式聊天交换器名字
     */
    String FANOUT_CHAT_NAME = "chat-fanout";
    /**
     * 服务交换器
     */
    String FANOUT_SERVICE_NAME = "service-fanout";
    /**
     * 通知服务交换器
     */
    String FANOUT_NOTICE_NAME = "notice-fanout";
    /**
     * 用户通知队列
     */
    String USER_NOTICE_QUEUE = "user_notice_queue";
    /**
     * 直连模式
     */
    String DIRECT_PATTERN = "direct";
    /**
     * 聊天主交换器
     */
    String CHAT_QUEUE_MAIN = "chat-queue-main";
    /**
     * 聊天辅交换器
     */
    String CHAT_QUEUE_ASSIST = "chat-queue-assist";
    /**
     * 服务主交换器
     */
    String SERVICE_QUEUE_MAIN = "service-queue-main";
    /**
     * 服务辅交换器
     */
    String SERVICE_QUEUE_ASSIST = "service-queue-assist";

    /**
     * 群聊队列1
     */
    String GROUP_CHAT_QUEUE_MAIN = "group-chat-main";
    /**
     * 群聊队列2
     */
    String GROUP_CHAT_QUEUE_ASSIST = "group-chat-assist";

    /**
     * 单聊类型
     */
    int BOX_TYPE_SINGLE_CHAT = 1;
    /**
     * 群聊类型
     */
    int BOX_TYPE_GROUP_CHAT = 2;
    /**
     * 好友申请类型
     */
    int BOX_TYPE_FRIEND_APPLICATION = 10;

    /**
     * 更新好友申请消息为已读
     */
    int BOX_TYPE_FRIEND_APPLICATION_READ = 11;
    /**
     * 更新单聊消息为已读
     */
    int BOX_TYPE_SINGLE_CHAT_READ = 20;

    /**
     * 用户注册
     */
    int BOX_TYPE_USER_REGISTER = 30;
}
