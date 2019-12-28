package com.xsdkj.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.Group;
import com.xsdkj.wechat.entity.chat.GroupNoSay;
import com.xsdkj.wechat.mapper.GroupMapper;
import com.xsdkj.wechat.mapper.GroupNoSayMapper;
import com.xsdkj.wechat.service.GroupService;
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
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private GroupNoSayMapper groupNoSayMapper;

    @Override
    public Long getNoSayTimesByUidAndGroupId(Integer uid, Integer groupId) {
        Object redisData = redisUtil.get(SystemConstant.REDIS_GROUP_NO_SAY);
        Long times;
        if (redisData == null) {
            times = groupNoSayMapper.findOneByUidAndGroupId(uid, groupId).getTimes();
            if (times == null) {
                return null;
            }
            Map<Integer, Map<Integer, Long>> noSayMap = new HashMap<>();
            Map<Integer, Long> map = new HashMap<>();
            map.put(groupId, times);
            noSayMap.put(uid, map);
            redisUtil.set(SystemConstant.REDIS_GROUP_NO_SAY, JSONObject.toJSONString(noSayMap));
        }
        assert redisData != null;
        Map map = JSONObject.toJavaObject(JSONObject.parseObject(redisData.toString()), Map.class);
        Map noSayMap = (Map) map.get(uid);
        times = Long.parseLong(noSayMap.get(groupId).toString());
        return times;
    }

    @Override
    public void updateRedisNoSayData() {
        Map<Integer, Map<Integer, Long>> noSayMap = new HashMap<>(100);
        List<GroupNoSay> groupNoSays = groupNoSayMapper.selectByAll(null);
        if (groupNoSays.size() > 0) {
            // 初始化map
            groupNoSays.forEach(groupNoSay -> {
                Map<Integer, Long> map = new HashMap<>(10);
                noSayMap.put(groupNoSay.getUid(), map);
            });
            // 为map赋值
            groupNoSays.forEach(groupNoSay -> noSayMap.get(groupNoSay.getUid()).put(groupNoSay.getGroupId(), groupNoSay.getTimes()));
        }
        redisUtil.set(SystemConstant.REDIS_GROUP_NO_SAY, JSONObject.toJSONString(noSayMap));
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
    public List<Group> listAllChatGroup() {
        List<Group> groups = groupMapper.selectByAll(null);
        groups.forEach(this::updateRedisGroupById);
        return groups;
    }

    @Override
    public void updateRedisGroupById(Integer groupId) {
        Group group = groupMapper.selectByPrimaryKey(groupId);
        List<ListMembersVo> listMembersVos = groupMapper.listGroupMembersByGroupId(group.getId());
        redisUtil.set(SystemConstant.REDIS_GROUP_KEY + group.getId(), JSONObject.toJSONString(group));
        redisUtil.set(SystemConstant.REDIS_GROUP_MEMBERS + group.getId(), JSONObject.toJSONString(listMembersVos));
    }

    @Override
    public void updateRedisGroupById(Group group) {
        List<ListMembersVo> listMembersVos = groupMapper.listGroupMembersByGroupId(group.getId());
        redisUtil.set(SystemConstant.REDIS_GROUP_KEY + group.getId(), JSONObject.toJSONString(group));
        redisUtil.set(SystemConstant.REDIS_GROUP_MEMBERS + group.getId(), JSONObject.toJSONString(listMembersVos));
    }

    @Override
    public List<GroupVo> listGroupByUid(Integer userId) {
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
        String redisData = redisUtil.get(SystemConstant.REDIS_GROUP_KEY + groupId).toString();
        Group group = JSONObject.toJavaObject(JSONObject.parseObject(redisData), Group.class);
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
        String redisData = redisUtil.get(SystemConstant.REDIS_GROUP_MEMBERS + groupId).toString();
        return JSONObject.parseArray(redisData, ListMembersVo.class);
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
        redisUtil.expire(SystemConstant.REDIS_GROUP_MEMBERS + groupId, 1);
        redisUtil.expire(SystemConstant.REDIS_GROUP_KEY + groupId, 1);
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
}
