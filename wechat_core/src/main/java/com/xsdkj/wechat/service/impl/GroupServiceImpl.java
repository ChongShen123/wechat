package com.xsdkj.wechat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.constant.RedisConstant;
import com.xsdkj.wechat.entity.chat.GroupNoSay;
import com.xsdkj.wechat.entity.chat.UserGroup;
import com.xsdkj.wechat.mapper.UserGroupMapper;
import com.xsdkj.wechat.mapper.GroupNoSayMapper;
import com.xsdkj.wechat.service.UserGroupService;
import com.xsdkj.wechat.service.ex.DataEmptyException;
import com.xsdkj.wechat.util.RedisUtil;
import com.xsdkj.wechat.vo.GroupBaseInfoVo;
import com.xsdkj.wechat.vo.GroupInfoVo;
import com.xsdkj.wechat.vo.GroupVo;
import com.xsdkj.wechat.vo.ListMembersVo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author tiankong
 * @date 2019/12/12 11:38
 */
@Service
public class GroupServiceImpl implements UserGroupService {
    @Resource
    private UserGroupMapper groupMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private GroupNoSayMapper groupNoSayMapper;

    @Override
    public Long getNoSayTimesByUidAndGroupId(Integer uid, Integer groupId) {
        Object redisData = redisUtil.get(RedisConstant.REDIS_GROUP_NO_SAY);
        Long times;
        if (redisData == null) {
            GroupNoSay oneByUidAndGroupId = groupNoSayMapper.findOneByUidAndGroupId(uid, groupId);
            if (oneByUidAndGroupId == null) {
                return null;
            }
            times = oneByUidAndGroupId.getTimes();
            if (times == null) {
                return null;
            }
            Map<Integer, Map<Integer, Long>> noSayMap = new HashMap<>();
            Map<Integer, Long> map = new HashMap<>();
            map.put(groupId, times);
            noSayMap.put(uid, map);
            redisUtil.set(RedisConstant.REDIS_GROUP_NO_SAY, JSONObject.toJSONString(noSayMap));
        }
        assert redisData != null;
        Map map = JSONObject.toJavaObject(JSONObject.parseObject(redisData.toString()), Map.class);
        Map noSayMap = (Map) map.get(uid);
        // 判断用户是否有在禁言黑名单内
        if (noSayMap == null) {
            return null;
        }
        // 判断用户在groupId 是否有禁言类型
        Object group = noSayMap.get(groupId);
        if (group == null) {
            return null;
        }
        times = Long.parseLong(group.toString());
        return times;
    }

    @Override
    public void updateRedisNoSayData() {
        Map<Integer, Map<Integer, Long>> noSayMap = new HashMap<>();
        List<GroupNoSay> groupNoSays = groupNoSayMapper.selectByAll(null);
        if (groupNoSays.size() > 0) {
            // 初始化map
            groupNoSays.forEach(groupNoSay -> {
                Map<Integer, Long> map = new HashMap<>();
                noSayMap.put(groupNoSay.getUid(), map);
            });
            // 为map赋值
            groupNoSays.forEach(groupNoSay -> noSayMap.get(groupNoSay.getUid()).put(groupNoSay.getGroupId(), groupNoSay.getTimes()));
        }
        redisUtil.set(RedisConstant.REDIS_GROUP_NO_SAY, JSONObject.toJSONString(noSayMap));
    }

    @Override
    public void saveNoSay(Integer uid, Integer groupId, Long times) {
        GroupNoSay noSay = groupNoSayMapper.findOneByUidAndGroupId(uid, groupId);
        if (noSay == null) {
            groupNoSayMapper.save(uid, groupId, times);
        }
        groupNoSayMapper.updateByUidAndGid(uid, groupId, times);
    }

    @Override
    public List<UserGroup> listAllChatGroup() {
        List<UserGroup> groups = groupMapper.selectByAll(null);
        groups.forEach(this::updateRedisGroupById);
        return groups;
    }

    @Override
    public void updateRedisGroupById(Integer groupId) {
        UserGroup group = groupMapper.selectByPrimaryKey(groupId);
        List<ListMembersVo> listMembersVos = groupMapper.listGroupMembersByGroupId(group.getId());
        redisUtil.set(RedisConstant.REDIS_GROUP_KEY + group.getId(), JSONObject.toJSONString(group));
        redisUtil.set(RedisConstant.REDIS_GROUP_MEMBERS + group.getId(), JSONObject.toJSONString(listMembersVos));
    }

    @Override
    public void updateRedisGroupById(UserGroup group) {
        List<ListMembersVo> listMembersVos = groupMapper.listGroupMembersByGroupId(group.getId());
        redisUtil.set(RedisConstant.REDIS_GROUP_KEY + group.getId(), JSONObject.toJSONString(group));
        redisUtil.set(RedisConstant.REDIS_GROUP_MEMBERS + group.getId(), JSONObject.toJSONString(listMembersVos));
    }

    @Override
    public List<GroupVo> listGroupByUid(Integer userId) {
        return groupMapper.listGroupByUserId(userId);
    }

    @Override
    public GroupBaseInfoVo getBaseInfo(Integer groupId) throws DataEmptyException {
        UserGroup group = getGroupById(groupId);
        if (group == null) {
            throw new DataEmptyException();
        }
        return createNewGroupBaseInfoVo(group);
    }

    private GroupBaseInfoVo createNewGroupBaseInfoVo(UserGroup group) {
        GroupBaseInfoVo groupBaseInfoVo = new GroupBaseInfoVo();
        groupBaseInfoVo.setAddFriendType(group.getAddFriendType());
        groupBaseInfoVo.setId(group.getId());
        groupBaseInfoVo.setMembersCount(group.getMembersCount());
        groupBaseInfoVo.setName(group.getName());
        groupBaseInfoVo.setNoSayType(group.getNoSayType());
        return groupBaseInfoVo;
    }

    @Override
    public GroupInfoVo getGroupInfo(Integer groupId) throws DataEmptyException {
        UserGroup group = getGroupById(groupId);
        if (group == null) {
            throw new DataEmptyException();
        }
        return createNewGroupInfo(group);
    }

    private GroupInfoVo createNewGroupInfo(UserGroup group) {
        GroupInfoVo result = new GroupInfoVo();
        result.setCreateTimes(group.getCreateTimes());
        result.setIcon(group.getIcon());
        result.setId(group.getId());
        result.setName(group.getName());
        result.setMembersCount(group.getMembersCount());
        result.setQr(group.getQr());
        result.setNotice(group.getNotice());
        result.setAddFriendType(group.getAddFriendType());
        result.setNoSayType(group.getNoSayType());
        return result;
    }

    @Override
    public void save(UserGroup group) {
        groupMapper.insert(group);
    }

    @Override
    public void update(UserGroup group) {
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
    public UserGroup getGroupById(Integer groupId) {
        Object redisData = redisUtil.get(RedisConstant.REDIS_GROUP_KEY + groupId);
        UserGroup group;
        if (redisData == null) {
            group = groupMapper.selectByPrimaryKey(groupId);
            if (group == null) {
                return null;
            }
            redisUtil.set(RedisConstant.REDIS_GROUP_KEY + group.getId(), JSONObject.toJSONString(group));
            return group;
        }
        group = JSONObject.toJavaObject(JSONObject.parseObject(redisData.toString()), UserGroup.class);
        return group;
    }

    @Override
    public void updateGroupCount(int num, Integer gid) {
        groupMapper.updateGroupCount(num, gid);
    }

    @Override
    public List<ListMembersVo> listGroupMembersByGroupId(Integer groupId) {
        Object redisData = redisUtil.get(RedisConstant.REDIS_GROUP_MEMBERS + groupId);
        if (ObjectUtil.isEmpty(redisData)) {
            List<ListMembersVo> listMembersVos = groupMapper.listGroupMembersByGroupId(groupId);
            if (listMembersVos.size() == 0) {
                throw new DataEmptyException();
            }
            redisUtil.set(RedisConstant.REDIS_GROUP_MEMBERS + groupId, JSONObject.toJSONString(listMembersVos));
            return listMembersVos;
        }
        String strData = redisData.toString();
        return JSONObject.parseArray(strData, ListMembersVo.class);
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
    public void deleteById(Integer groupId) {
        groupMapper.deleteByPrimaryKey(groupId);
    }

    @Override
    public void deleteRedisData(Integer groupId) {
        redisUtil.expire(RedisConstant.REDIS_GROUP_MEMBERS + groupId, 1);
        redisUtil.expire(RedisConstant.REDIS_GROUP_KEY + groupId, 1);
    }

    @Override
    public void addGroupManager(Integer groupId, Integer userId) throws DataIntegrityViolationException {
        groupMapper.setGroupManager(groupId, userId);
    }

    @Override
    public List<Integer> listGroupManagerByUserId(Integer groupId) {
        return groupMapper.listGroupManagerByUserId(groupId);
    }

    @Override
    public Integer countGroupManger(Integer groupId, Integer uid) {
        return groupMapper.countGroupManger(groupId, uid);
    }

    @Override
    public void deleteGroupManager(Integer groupId, Integer userId) {
        groupMapper.deleteGroupManager(groupId, userId);
    }

    @Override
    public void relieveNoSay(Integer userId, Integer groupId) {
        groupMapper.deleteNoSayByUidAndGid(userId, groupId);
    }

    @Override
    public void setGroupChat(Integer groupId, Integer type) {
        groupMapper.setGroupChat(groupId, type);
    }

    @Override
    public void updateGroupInfo(Integer groupId, String name, String icon, String notice) {
        groupMapper.updateGroupInfo(groupId, name, icon, notice);
    }

    @Override
    public void updateAddFriend(Integer groupId, Boolean addFriend) {
        groupMapper.updateAddFriend(groupId, addFriend);
    }
}
