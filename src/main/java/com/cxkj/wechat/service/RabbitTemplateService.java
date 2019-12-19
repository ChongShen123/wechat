package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.ChatInfo;

/**
 * @author tiankong
 * @date 2019/12/13 13:55
 */
public interface RabbitTemplateService {
    /**
     * 单聊消息进入队列
     *
     * @param queue   队列名称
     * @param content 内容
     */
    void addChatInfo(String queue, ChatInfo content);
}
