package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.mapper.GroupMapper;
import com.cxkj.wechat.service.GroupService;
import com.cxkj.wechat.service.cache.GroupCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 11:38
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupCache groupCache;

    @Override
    public List<Group> listAllChatGroup() {
        return groupMapper.selectByAll(null);
    }

    @Override
    public void save(Group group) {
        groupMapper.insert(group);
    }

    @Override
    public void update(Group group) {
        groupMapper.updateByPrimaryKeySelective(group);
        groupCache.update(group);
    }

    @Override
    public void updateQr(Integer id, String generate) {
        groupMapper.updateQr(id, generate);
        groupCache.update(groupMapper.getById(id));
    }
    @Override
    public void insertUserIds(List<Integer> ids, Integer groupId) {
        groupMapper.insertUserIds(ids, groupId);
    }
}
