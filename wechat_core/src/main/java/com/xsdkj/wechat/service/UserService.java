package com.xsdkj.wechat.service;


import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.bo.PermissionBo;
import com.xsdkj.wechat.dto.UserLoginDto;
import com.xsdkj.wechat.dto.UserRegisterDto;
import com.xsdkj.wechat.dto.UserUpdateInfoParam;
import com.xsdkj.wechat.dto.UserUpdatePassword;
import com.xsdkj.wechat.entity.chat.User;
import com.xsdkj.wechat.vo.ListUserFriendVo;
import com.xsdkj.wechat.vo.LoginVo;
import com.xsdkj.wechat.vo.admin.LoginInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/11 13:08
 */
public interface UserService {
    /**
     * 查询用户
     *
     * @param uno 用户系统账号
     * @return 用户
     */
    User getByUsername(String uno);

    /**
     * 登录
     *
     * @param param   参数
     * @param request 连接
     * @param check   是否验证密码
     * @return 登录信息
     */
    LoginVo login(UserLoginDto param, HttpServletRequest request, boolean check);

    /**
     * 用户注册
     *
     * @param param   参数
     * @param request 连接信息
     * @return 登录信息
     */
    LoginVo register(UserRegisterDto param, HttpServletRequest request);

    /**
     * 查询用户
     *
     * @param ids ids
     * @return List
     */
    List<User> listUserByIds(Set<Integer> ids);

    /**
     * 查询用户
     *
     * @param id id
     * @return 用户
     */
    User getByUserId(Integer id);

    /**
     * 修改用户的基本信息
     *
     * @param param param
     */
    void updateUserInfo(UserUpdateInfoParam param);

    /**
     * 修改用户的密码
     */
    void updatePassword(UserUpdatePassword password);

    /**
     * 后台管理员登录 获取管理员信息
     *
     * @return LoginInfoVo
     */
    LoginInfoVo getInfo();

    /**
     * 获取用户菜单
     *
     * @param id 用户id
     * @return list
     */
    List<PermissionBo> getUserPermission(Integer id);

    /**
     * 创建一个当前用户对象
     *
     * @param user 用户
     * @return UserDetailsBo
     */
    UserDetailsBo createCurrentUserDetailsBo(User user);

    /**
     * 查看好友列表
     *
     * @param uid 用户ID
     * @return 好友列表
     */
    List<ListUserFriendVo> listFriendByUserId(Integer uid);

    /**
     * 更新redis 用户好友列表
     *
     * @param uid 用户ID
     * @return 好友列表
     */
    List<ListUserFriendVo> updateRedisListFriendByUserId(Integer uid);

    /**
     * 删除好友
     *
     * @param uid      用户ID
     * @param friendId 好友ID
     */
    void deleteFriend(Integer uid, Integer friendId);
}
