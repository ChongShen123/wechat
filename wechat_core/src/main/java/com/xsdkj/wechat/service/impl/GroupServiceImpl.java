package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.Group;
import com.xsdkj.wechat.mapper.GroupMapper;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.ex.DataEmptyException;
import com.xsdkj.wechat.service.GroupService;
import com.xsdkj.wechat.util.RedisUtil;
import com.xsdkj.wechat.vo.GroupBaseInfoVo;
import com.xsdkj.wechat.vo.GroupInfoVo;
import com.xsdkj.wechat.vo.ListGroupVo;
import com.xsdkj.wechat.vo.ListMembersVo;
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
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<Group> listAllChatGroup() {
        List<Group> groups = groupMapper.selectByAll(null);
        groups.forEach(group -> redisUtil.set(SystemConstant.REDIS_GROUP_KEY + group.getId(), group));
        return groups;
    }

    @Override
    public List<ListGroupVo> listGroupByUid(Integer userId) {
        return groupMapper.listGroupByUserId(userId);
    }

    @Override
    public GroupBaseInfoVo getBaseInfo(Integer groupId) throws DataEmptyException {
        Group group = getGroupById(groupId);
        if (group == null) {
            throw new DataEmptyException();
        }
        return createNewGroupBaseInfoVo(group);
    }

    private GroupBaseInfoVo createNewGroupBaseInfoVo(Group group) {
        GroupBaseInfoVo groupBaseInfoVo = new GroupBaseInfoVo();
        groupBaseInfoVo.setAddFriendType(group.getAddFriendType());
        groupBaseInfoVo.setId(group.getId());
        groupBaseInfoVo.setMembersCount(group.getMembersCount());
        groupBaseInfoVo.setName(group.getName());
        return groupBaseInfoVo;
    }

    @Override
    public GroupInfoVo getGroupInfo(Integer groupId) throws DataEmptyException {
        Group group = getGroupById(groupId);
        if (group == null) {
            throw new DataEmptyException();
        }
        return createNewGroupInfo(group);
    }

    private GroupInfoVo createNewGroupInfo(Group group) {
        GroupInfoVo result = new GroupInfoVo();
        result.setCreateTimes(group.getCreateTimes());
        result.setIcon(group.getIcon());
        result.setId(group.getId());
        result.setName(group.getName());
        result.setMembersCount(group.getMembersCount());
        result.setQr(group.getQr());
        result.setNotice(group.getNotice());
        result.setAddFriendType(group.getAddFriendType());
        return result;
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
        Group group = (Group) redisUtil.get(SystemConstant.REDIS_GROUP_KEY + groupId);
        if (group == null) {
            group = groupMapper.selectByPrimaryKey(groupId);
            if (group == null) {
                return null;
            }
            redisUtil.set(SystemConstant.REDIS_GROUP_KEY + group.getId(), group);
        }
        return group;
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

    @Override
    public void updateRedisGroupByGroupId(Integer groupId) {
        Group group = groupMapper.selectByPrimaryKey(groupId);
        if (group != null) {
            redisUtil.set(SystemConstant.REDIS_GROUP_KEY + group.getId(), group);
        }
    }
}
