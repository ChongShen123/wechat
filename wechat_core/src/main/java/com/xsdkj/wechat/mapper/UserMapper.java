package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.bo.PermissionBo;
import com.xsdkj.wechat.bo.UserBo;
import com.xsdkj.wechat.dto.ListUserDto;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.vo.UserFriendVo;
import com.xsdkj.wechat.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/30 17:26
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
    List<UserFriendVo> listFriendByUserId(@Param("uid") Integer uid);

    /**
     * 删除好友
     *
     * @param uid      用户ID
     * @param friendId 好友ID
     */
    void deleteFriend(@Param("uid") Integer uid, @Param("friendId") Integer friendId);

    /**
     * 修改用户登录状态
     *
     * @param uid  用户id
     * @param type 登录状态
     */
    void updateLoginState(@Param("uid") Integer uid, @Param("type") Boolean type);

    /**
     * 检查用户是否存在
     *
     * @param userIds 用户ids
     * @return count
     */
    int countUserIds(@Param("userIds") Set<Integer> userIds);

    /**
     * condition query user list
     *
     * @param listUserDto parameter
     * @return list UserVo
     */
    List<UserVo> listUser(ListUserDto listUserDto);


}
