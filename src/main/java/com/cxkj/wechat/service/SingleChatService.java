package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.SingleChat;

public interface SingleChatService {
    void save(SingleChat singleChat);

    void deleteTask(SingleChat singleChat);

    /**
     * 查询单聊消息
     * @param id id
     * @return 消息
     */
    SingleChat getById(String id);
}
