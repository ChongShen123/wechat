package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.mapper.GroupMapper;
import com.cxkj.wechat.service.GroupService;
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

    @Override
    public List<Group> listAllChatGroup() {
        return groupMapper.selectByAll(null);
    }
}
