package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.Group;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 11:37
 */
public interface GroupService {

    List<Group> listAllChatGroup();

    /**
     * 创建群聊
     *
     * @param group group
     */
    void save(Group group);

    /**
     * 修改群信息
     *
     * @param group group
     * @return group
     */
    void update(Group group);

    /**
     * 保存用户与群组关系
     *
     * @param ids     用户id
     * @param groupId 群id
     */
    void insertUserIds(List<Integer> ids, Integer groupId);

    /**
     * 更新二维码
     * @param id id
     * @param generate 二维码图片
     */
    void updateQr(Integer id, String generate);
}
