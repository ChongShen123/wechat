package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.bo.GroupInfo;
import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.mapper.GroupMapper;
import com.cxkj.wechat.netty.ex.DataEmptyException;
import com.cxkj.wechat.service.GroupService;
import com.cxkj.wechat.vo.GroupBaseInfoVO;
import com.cxkj.wechat.vo.GroupInfoVO;
import com.cxkj.wechat.vo.ListGroupVO;
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

    @Override
    public List<ListGroupVO> listGroupByUid(Integer userId) {
        return groupMapper.listGroupByUserId(userId);
    }

    @Override
    public GroupBaseInfoVO getBaseInfo(Integer groupId) {
        GroupBaseInfoVO baseInfo = groupMapper.getBaseInfo(groupId);
        System.out.println(baseInfo);
        return baseInfo;
    }

    @Override
    public GroupInfoVO getGroupInfo(Integer groupId) throws DataEmptyException {
        GroupInfoVO info = groupMapper.getInfo(groupId);
        if (info == null) {
            throw new DataEmptyException();
        }
        return info;
    }

    @Override
    public void save(Group group) {
        groupMapper.insert(group);
    }

    @Override
    public void update(Group group) {
        groupMapper.updateByPrimaryKeySelective(group);
    }

    @Override
    public void updateQr(Integer id, String generate) {
        groupMapper.updateQr(id, generate);
    }

    @Override
    public void insertUserIds(List<Integer> ids, Integer groupId) {
        groupMapper.insertUserIds(ids, groupId);
    }
}
