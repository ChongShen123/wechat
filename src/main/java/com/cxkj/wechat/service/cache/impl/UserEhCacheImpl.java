package com.cxkj.wechat.service.cache.impl;

import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.mapper.UserMapper;
import com.cxkj.wechat.service.cache.UserCache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/11 19:37
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserEhCacheImpl implements UserCache {
    @Resource
    private UserMapper userMapper;

    @Override
    @Cacheable(key = "'username_'+#p0")
    public User getByUsername(String username) {
        return userMapper.getOneByUsername(username);
    }

    @Override
    @Cacheable(key = "'user_id_'+#p0")
    public User getByUserId(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
