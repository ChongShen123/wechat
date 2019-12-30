package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.chat.UserGroup;
import com.xsdkj.wechat.service.ex.DataEmptyException;
import com.xsdkj.wechat.vo.GroupBaseInfoVo;
import com.xsdkj.wechat.vo.GroupInfoVo;
import com.xsdkj.wechat.vo.GroupVo;
import com.xsdkj.wechat.vo.ListMembersVo;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/12 11:37
 */
public interface UserGroupService {

    /**
     * 获取用户禁言时间
     *
     * @param uid     用户id
     * @param groupId 群组id
     * @return GroupNoSay
     */
    Long getNoSayTimesByUidAndGroupId(Integer uid, Integer groupId);

    /**
     * 更新redis群组禁言黑名单
     */
    void updateRedisNoSayData();

    /**
     * 保存一个禁言用户
     *
     * @param uid     用户uid
     * @param groupId 群组id
     * @param times   禁言时间
     */
    void saveNoSay(Integer uid, Integer groupId, Long times);

    /**
     * 查看所有群
     *
     * @return List
     */
    List<UserGroup> listAllChatGroup();

    /**
     * 更新redis群组信息
     *
     * @param groupId 群组id
     */
    void updateRedisGroupById(Integer groupId);

    /**
     * 更新redis群组信息
     *
     * @param group 群组id
     */
    void updateRedisGroupById(UserGroup group);

    /**
     * 创建群聊
     *
     * @param group group
     */
    void save(UserGroup group);

    /**
     * 修改群信息
     *
     * @param group group
     */
    void update(UserGroup group);

    /**
     * 保存用户与群组关系
     *
     * @param ids     用户id
     * @param groupId 群id
     */
    void insertUserIds(Set<Integer> ids, Integer groupId);

    /**
     * 更新二维码
     *
     * @param id       id
     * @param generate 二维码图片
     */
    void updateQr(Integer id, String generate);


    /**
     * 查询用户所有群组
     *
     * @param userId 用户id
     * @return list
     */
    List<GroupVo> listGroupByUid(Integer userId);

    /**
     * 获取群组基本信息
     *
     * @param groupId 群ID
     * @return groupBaseInfoVO
     * @throws DataEmptyException 未找到该群异常
     */
    GroupBaseInfoVo getBaseInfo(Integer groupId) throws DataEmptyException;

    /**
     * 获取群详情
     *
     * @param groupId groupId
     * @return GroupInfoBo
     * @throws DataEmptyException 未找到该群异常
     */
    GroupInfoVo getGroupInfo(Integer groupId) throws DataEmptyException;

    /**
     * 获取群完整信息
     *
     * @param groupId groupId
     * @return Group
     */
    UserGroup getGroupById(Integer groupId);

    /**
     * 更新群总人数
     *
     * @param num 个数(正为增加 负为减少)
     * @param gid 群ID
     */
    void updateGroupCount(int num, Integer gid);

    /**
     * 查看群所有成员
     *
     * @param groupId 群ID
     * @return 所有成员
     */
    List<ListMembersVo> listGroupMembersByGroupId(Integer groupId);

    /**
     * 检查用户是否入群
     *
     * @param uid     用户ID
     * @param groupId 群ID
     * @return boolean
     */
    Integer checkUserJoined(Set<Integer> uid, Integer groupId);

    /**
     * 查询用户群聊
     *
     * @param groupId 群ID
     * @param uid     用户ID
     * @return UserGroupRelation
     */
    String getNicknameByGroupIdAndUid(Integer groupId, Integer uid);

    /**
     * 退群
     *
     * @param ids     用户id
     * @param groupId 群id
     */
    void quitGroup(Set<Integer> ids, Integer groupId);


    /**
     * 删除redis数据
     *
     * @param groupId 群组id
     */
    void deleteRedisData(Integer groupId);

    /**
     * 删除群组
     *
     * @param groupId 群组id
     */
    void deleteById(Integer groupId);


    /**
     * 添加群管理员
     *
     * @param groupId 群组id
     * @param userId  用户id
     * @throws DataIntegrityViolationException 指定的用户不存在,抛出的外键关联失败异常
     */
    void addGroupManager(Integer groupId, Integer userId) throws DataIntegrityViolationException;

    /**
     * 查询群管理人员
     *
     * @param groupId 群id
     * @return 管理员id
     */
    List<Integer> listGroupManagerByUserId(Integer groupId);

    /**
     * 判断用户是是否已为管理员
     *
     * @param groupId 群id
     * @param uid     用户id
     * @return int
     */
    Integer countGroupManger(Integer groupId, Integer uid);

    /**
     * 删除群管理员
     *
     * @param groupId 群id
     * @param userId  用户id
     */
    void deleteGroupManager(Integer groupId, Integer userId);

    /**
     * 解除用户禁言
     *
     * @param userId  用户id
     * @param groupId 群id
     */
    void relieveNoSay(Integer userId, Integer groupId);

    /**
     * 设置群是否开起聊天
     *
     * @param groupId 群id
     * @param type 0 关闭 1开启
     */
    void setGroupChat(Integer groupId, Integer type);
}
