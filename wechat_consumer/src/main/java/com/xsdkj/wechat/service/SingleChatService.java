package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.chat.SingleChat;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/27 10:23
 */
public interface SingleChatService {
    void save(SingleChat singleChat);

    /**
     * 更新单聊阅读状态
     * @param read 阅读状态
     * @param singleChats list
     */
    void updateRead(boolean read, List<SingleChat> singleChats);
}
