package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.Friend;

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
