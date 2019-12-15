package com.cxkj.wechat.service.cache;

import com.cxkj.wechat.bo.UserGroup;
import com.cxkj.wechat.entity.Group;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 11:01
 */
public interface GroupCache {
    /**
     * 查询用户群组
     *
     * @param uid 用户id
     * @return 用户群组
     */
    List<UserGroup> listGroupByUid(Integer uid);

    /**
     * 更新缓存
     *
     * @param group group
     * @return group
     */
    Group update(Group group);

    /**
     * 查询群组信息
     * @param id id
     * @return group
     */
    Group getById(Integer id);

}
