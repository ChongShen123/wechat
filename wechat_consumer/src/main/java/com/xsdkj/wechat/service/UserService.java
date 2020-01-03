package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.user.User;

/**
 * @author tiankong
 * @date 2020/1/2 10:30
 */
public interface UserService {
    /**
     * 保存用户信息
     * @param user 用户
     */
    void save(User user);
}
