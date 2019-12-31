package com.xsdkj.wechat.service;


import com.xsdkj.wechat.entity.chat.FriendApplication;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 13:56
 */
public interface FriendApplicationService {

    /**
     * 保存好友申请
     *
     * @param application application
     */
    void save(FriendApplication application);

    /**
     * 查询是否为好友
     *
     * @param toUserId   toUserId
     * @param formUserId formUserId
     * @return Long
     */
    Long countByToUserIdAndFromUserId(Integer toUserId, Integer formUserId);

    /**
     * 查询好友申请
     *
     * @param id id
     * @return 好友申请
     */
    FriendApplication getFriendApplicationById(String id);

    /**
     * 查询所有好友申请
     *
     * @param userId 用户id
     * @return list
     */
    List<FriendApplication> listByUserId(Integer userId);

    /**
     * 查询好友申请表
     *
     * @param isRead 阅读状态
     * @param uid   用户id
     * @return list
     */
    List<FriendApplication> listByReadAndUserId(boolean isRead, Integer uid);

    /**
     * 修改好友申请
     *
     * @param application application
     */
    void update(FriendApplication application);

    /**
     * 删除聊天记录
     *
     * @param id id
     */
    void deleteById(String id);
}
