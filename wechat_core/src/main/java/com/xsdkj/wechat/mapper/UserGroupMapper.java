package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.chat.UserGroup;
import com.xsdkj.wechat.vo.GroupBaseInfoVo;
import com.xsdkj.wechat.vo.GroupInfoVo;
import com.xsdkj.wechat.vo.GroupVo;
import com.xsdkj.wechat.vo.ListMembersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/30 11:55
 */
public interface UserGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGroup record);

    int insertSelective(UserGroup record);

    UserGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGroup record);

    int updateByPrimaryKey(UserGroup record);

    List<UserGroup> selectByAll(UserGroup group);

    void insertUserIds(@Param("ids") Set<Integer> ids, @Param("groupId") Integer groupId);

    void updateQr(@Param("id") Integer id, @Param("qr") String qr);

    UserGroup getById(@Param("id") Integer id);

    List<GroupVo> listGroupByUserId(Integer userId);

    GroupBaseInfoVo getBaseInfo(@Param("groupId") Integer groupId);

    GroupInfoVo getInfo(@Param("groupId") Integer groupId);

    void updateGroupCount(@Param("num") int num, @Param("gid") Integer gid);

    List<ListMembersVo> listGroupMembersByGroupId(Integer groupId);

    Integer checkUserJoined(@Param("uids") Set<Integer> uids, @Param("groupId") Integer groupId);

    String getByGroupIdAndUid(@Param("groupId") Integer groupId, @Param("uid") Integer uid);

    void quitGroup(@Param("ids") Set<Integer> ids, @Param("groupId") Integer groupId);

    void setGroupManager(@Param("groupId") Integer groupId, @Param("userId") Integer userId);

    List<Integer> listGroupManagerByUserId(Integer groupId);

    Integer countGroupManger(@Param("groupId") Integer groupId, @Param("uid") Integer uid);

    void deleteGroupManager(@Param("groupId") Integer groupId, @Param("userId") Integer userId);

    void deleteNoSayByUidAndGid(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    void setGroupChat(@Param("groupId") Integer groupId, @Param("type") Integer type);

    void updateGroupInfo(@Param("groupId") Integer groupId, @Param("name") String name, @Param("icon") String icon, @Param("notice") String notice);

}
