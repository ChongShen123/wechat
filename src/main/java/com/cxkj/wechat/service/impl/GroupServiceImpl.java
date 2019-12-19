package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.entity.UserGroupRelation;
import com.cxkj.wechat.mapper.GroupMapper;
import com.cxkj.wechat.netty.ex.DataEmptyException;
import com.cxkj.wechat.service.GroupService;
import com.cxkj.wechat.vo.GroupBaseInfoVo;
import com.cxkj.wechat.vo.GroupInfoVo;
import com.cxkj.wechat.vo.ListGroupVo;
import com.cxkj.wechat.vo.ListMembersVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
    public List<ListGroupVo> listGroupByUid(Integer userId) {
        return groupMapper.listGroupByUserId(userId);
    }

    @Override
    public GroupBaseInfoVo getBaseInfo(Integer groupId) {
        return groupMapper.getBaseInfo(groupId);
    }

    @Override
    public GroupInfoVo getGroupInfo(Integer groupId) throws DataEmptyException {
        GroupInfoVo info = groupMapper.getInfo(groupId);
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
    public void insertUserIds(Set<Integer> ids, Integer groupId) {
        groupMapper.insertUserIds(ids, groupId);
    }

    @Override
    public Group getGroupById(Integer groupId) {
        return groupMapper.selectByPrimaryKey(groupId);
    }

    @Override
    public void updateGroupCount(int num, Integer gid) {
        groupMapper.updateGroupCount(num, gid);
    }

    @Override
    public List<ListMembersVo> listGroupMembersByGroupId(Integer groupId) {
        return groupMapper.listGroupMembersByGroupId(groupId);
    }

    @Override
    public Integer checkUserJoined(Set<Integer> uid, Integer groupId) {
        return groupMapper.checkUserJoined(uid, groupId);
    }

    @Override
    public String getNicknameByGroupIdAndUid(Integer groupId, Integer uid) {
        return groupMapper.getByGroupIdAndUid(groupId, uid);
    }

    @Override
    public void quitGroup(Set<Integer> ids, Integer groupId) {
        groupMapper.quitGroup(ids, groupId);
    }
}
