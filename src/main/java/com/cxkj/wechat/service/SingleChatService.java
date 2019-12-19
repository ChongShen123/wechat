package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.SingleChat;

/**
 * @author tiankong
 */
public interface SingleChatService {
     void save(SingleChat singleChat);

<<<<<<< HEAD
     void deleteTask();

     void deleteImage();
=======
    void deleteTask(SingleChat singleChat);

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
>>>>>>> c5c3ed6da5f1a81a762f2458cafc7f77fd431c9b
}
