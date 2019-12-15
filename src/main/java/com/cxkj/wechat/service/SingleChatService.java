package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.SingleChat;

public interface SingleChatService {
    void save(SingleChat singleChat);

    void deleteTask(SingleChat singleChat);
}
