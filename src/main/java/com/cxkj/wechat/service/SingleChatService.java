package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.SingleChat;

/**
 * @author tiankong
 */
public interface SingleChatService {
     void save(SingleChat singleChat);


     void deleteTask();

     void deleteImage();



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
