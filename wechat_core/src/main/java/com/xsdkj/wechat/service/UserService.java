package com.xsdkj.wechat.service;


import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.bo.PermissionBo;
import com.xsdkj.wechat.dto.*;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.vo.GroupVo;
import com.xsdkj.wechat.vo.UserFriendVo;
import com.xsdkj.wechat.vo.LoginVo;
import com.xsdkj.wechat.vo.UserVo;
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
     * @param username 用户名
     * @return 用户
     */
    User getByUsername(String username);

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
     * 用户注册
     *
     * @param param   参数
     * @param request 连接信息
     */
//    void register(UserRegisterDto param, HttpServletRequest request);

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
    User getRedisUserByUserId(Integer id);

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
    List<UserFriendVo> listFriendByUid(Integer uid);


    /**
     * 删除好友
     *
     * @param uid      用户ID
     * @param friendId 好友ID
     */
    void deleteFriend(Integer uid, Integer friendId);
    UserDetailsBo updateRedisDataByUid(Integer uid);
    /**
     * 更新用户Redis 数据
     * 更新用户菜单,用户群组,用户信息
     *
     * @param uid        用户id
     * @param methodName 调用方
     * @return List
     */
    UserDetailsBo updateRedisDataByUid(Integer uid, String methodName);

    /**
     * 更新用户Redis 数据
     * 更新用户菜单,用户群组,用户信息
     *
     * @param uid    用户id
     * @param wallet 用户钱包
     * @return List
     */
    UserDetailsBo updateRedisDataByUid(Integer uid, Wallet wallet);

    /**
     * 更新用户Redis 数据
     *
     * @param uid           用户id
     * @param userDetailsBo 用户数据
     * @return UserDetailsBo
     */
    UserDetailsBo updateRedisDataByUid(Integer uid, UserDetailsBo userDetailsBo);

    /**
     * 更新用户Redis 数据
     * 更新用户菜单,用户群组,用户信息
     *
     * @param user       用户
     * @param methodName 调用方方法名
     * @return UserDetailsBo
     */
    UserDetailsBo updateRedisDataByUid(User user, String methodName);

    /**
     * 获取用户缓存信息
     *
     * @param uid 用户id
     * @return UserDetailsBo
     */
    UserDetailsBo getRedisDataByUid(Integer uid);

    /**
     * 获取用户redis群组信息
     *
     * @param uid 用户id
     * @param gid 群组id
     * @return GroupVo
     */
    GroupVo getUserRedisGroup(Integer uid, Integer gid);

    /**
     * 查询本地用户
     *
     * @param userId 用户iD
     * @param type   是否更新用户redis缓存
     * @return User
     */
    User getUserById(Integer userId, boolean type);

    /**
     * 修改用户登录状态
     *
     * @param uid  用户id
     * @param type 登录状态
     */
    void updateLoginState(Integer uid, Boolean type);

    /**
     * 检查用户是否存在
     *
     * @param userIds 用户id
     * @return 计数
     */
    int countUserIds(Set<Integer> userIds);

    /**
     * Conditional query user list
     *
     * @param listUserDto parameter
     * @return List UserVo
     */
    List<UserVo> listUser(ListUserDto listUserDto);
}
