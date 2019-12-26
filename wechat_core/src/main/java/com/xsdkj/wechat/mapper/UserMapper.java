package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.bo.PermissionBo;import com.xsdkj.wechat.entity.chat.User;import com.xsdkj.wechat.vo.ListUserFriendVo;import org.apache.ibatis.annotations.Param;import java.util.List;import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/26 14:23
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Integer countByUno(@Param("uno") Long uno);

    User getOneByUsername(@Param("username") String username);

    User getOneByUno(@Param("uno") Long uno);

    Integer countByEmail(@Param("email") String email);

    List<User> listUserByIds(@Param("ids") Set<Integer> ids);

    List<PermissionBo> listPermissionByUid(@Param("uid") Integer uid);

    /**
     * 查询好友列表
     *
     * @param uid 用户ID
     * @return List
     */
    List<ListUserFriendVo> listFriendByUserId(@Param("uid") Integer uid);

    /**
     * 删除好友
     *
     * @param uid      用户ID
     * @param friendId 好友ID
     */
    void deleteFriend(@Param("uid") Integer uid, @Param("friendId") Integer friendId);
}