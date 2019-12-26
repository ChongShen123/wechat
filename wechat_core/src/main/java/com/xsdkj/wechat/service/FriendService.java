package com.xsdkj.wechat.service;


import com.xsdkj.wechat.entity.chat.Friend;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 18:57
 */
public interface FriendService {
    /**
     * 保存好友关系
     *
     * @param friends 好友关系
     */
    void saveList(List<Friend> friends);
}
