package com.cxkj.wechat.service.cache.impl;

import com.cxkj.wechat.bo.UserGroup;
import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.mapper.GroupMapper;
import com.cxkj.wechat.service.cache.GroupCache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 11:05
 */
@Service
@CacheConfig(cacheNames = "chat_group")
public class GroupCacheImpl implements GroupCache {
    @Resource
    private GroupMapper groupMapper;

    @Override
    @Cacheable(key = "'listGroup_'+#p0")
    public List<UserGroup> listGroupByUid(Integer uid) {
        return groupMapper.listGroupByUid(uid);
    }

    @Override
    @CachePut(key = "'getById'+#p0.id")
    public Group update(Group group) {
        return group;
    }

    @Override
    @Cacheable(key = "'getById'+#id")
    public Group getById(Integer id) {
        return groupMapper.selectByPrimaryKey(id);
    }
}
