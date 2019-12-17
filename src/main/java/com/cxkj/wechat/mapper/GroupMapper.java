package com.cxkj.wechat.mapper;

import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.entity.UserGroupRelation;
import com.cxkj.wechat.vo.GroupBaseInfoVo;
import com.cxkj.wechat.vo.GroupInfoVo;
import com.cxkj.wechat.vo.ListGroupVo;
import com.cxkj.wechat.vo.ListMembersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/14 18:51
 */
public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);


    List<Group> selectByAll(Group group);

    void insertUserIds(@Param("ids") Set<Integer> ids, @Param("groupId") Integer groupId);

    void updateQr(@Param("id") Integer id, @Param("qr") String qr);

    Group getById(@Param("id") Integer id);

    List<ListGroupVo> listGroupByUserId(Integer userId);

    GroupBaseInfoVo getBaseInfo(@Param("groupId") Integer groupId);

    GroupInfoVo getInfo(@Param("groupId") Integer groupId);

    void updateGroupCount(@Param("num") int num, @Param("gid") Integer gid);

    List<ListMembersVo> listGroupMembersByGroupId(Integer groupId);

    Integer checkUserJoined(@Param("uids") Set<Integer> uids, @Param("groupId") Integer groupId);

    UserGroupRelation getByGroupIdAndUid(@Param("groupId") Integer groupId, @Param("uid") Integer uid);

    void quitGroup(@Param("ids") Set<Integer> ids, @Param("groupId") Integer groupId);
}
