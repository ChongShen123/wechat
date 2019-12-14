package com.cxkj.wechat.service.cache;

import com.cxkj.wechat.entity.User;

/**
 * 用户相关的缓存接口
 *
 * @author tiankong
 * @date 2019/12/11 19:44
 */
public interface UserCache {
    /**
     * 查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User getByUsername(String username);

    /**
     * 查询用户
     *
     * @param userId 用户id
     * @return 用户
     */
    User getByUserId(Integer userId);
}
