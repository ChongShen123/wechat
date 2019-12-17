package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.entity.UserGroupRelation;
import com.cxkj.wechat.netty.ex.DataEmptyException;
import com.cxkj.wechat.vo.GroupBaseInfoVo;
import com.cxkj.wechat.vo.GroupInfoVo;
import com.cxkj.wechat.vo.ListGroupVo;
import com.cxkj.wechat.vo.ListMembersVo;

import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/12 11:37
 */
public interface GroupService {

    List<Group> listAllChatGroup();

    /**
     * 创建群聊
     *
     * @param group group
     */
    void save(Group group);

    /**
     * 修改群信息
     *
     * @param group group
     */
    void update(Group group);

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
    List<ListGroupVo> listGroupByUid(Integer userId);

    /**
     * 获取群组基本信息
     *
     * @param groupId 群ID
     * @return groupBaseInfoVO
     */
    GroupBaseInfoVo getBaseInfo(Integer groupId);

    /**
     * 获取群详情
     *
     * @param groupId groupId
     * @return GroupInfoBo
     */
    GroupInfoVo getGroupInfo(Integer groupId) throws DataEmptyException;

    /**
     * 获取群完整信息
     *
     * @param groupId groupId
     * @return Group
     */
    Group getGroupById(Integer groupId);

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
    UserGroupRelation getNicknameByGroupIdAndUid(Integer groupId, Integer uid);

    /**
     * 退群
     *
     * @param ids 用户id
     * @param groupId 群id
     */
    void quitGroup(Set<Integer> ids, Integer groupId);
}
