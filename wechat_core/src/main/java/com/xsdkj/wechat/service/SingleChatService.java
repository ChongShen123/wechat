package com.xsdkj.wechat.service;


import com.xsdkj.wechat.entity.chat.SingleChat;

/**
 * @author tiankong
 */
public interface SingleChatService {
     void save(SingleChat singleChat);

     void deleteSingleChat();

    /**
     * 查询单聊消息
     *
     * @param id id
     * @return 消息
     */
    SingleChat getById(String id);

    /**
     * 删除消息
     *
     * @param id 消息id
     */
    void deleteById(String id);

}
