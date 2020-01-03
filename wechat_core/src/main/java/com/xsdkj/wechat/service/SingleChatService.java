package com.xsdkj.wechat.service;


import com.xsdkj.wechat.entity.chat.SingleChat;

import java.util.List;

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
     * 查看用户离线消息
     *
     * @param read     阅读状态
     * @param toUserId 用户id
     * @return List
     */
    List<SingleChat> listByReadAndToUserId(boolean read, Integer toUserId);


    /**
     * 删除消息
     *
     * @param id 消息id
     */
    void deleteById(String id);

}
