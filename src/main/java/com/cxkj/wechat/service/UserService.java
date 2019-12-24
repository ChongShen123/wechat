package com.cxkj.wechat.service;

import com.cxkj.wechat.bo.CurrentUserDetailsBo;
import com.cxkj.wechat.bo.PermissionBo;
import com.cxkj.wechat.dto.UserUpdateInfoParam;
import com.cxkj.wechat.dto.UserUpdatePassword;
import com.cxkj.wechat.vo.LoginVo;
import com.cxkj.wechat.dto.UserLoginDto;
import com.cxkj.wechat.dto.UserRegisterDto;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.vo.admin.LoginInfoVo;

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
     * @return CurrentUserDetailsBo
     */
    CurrentUserDetailsBo createCurrentUserDetailsBo(User user);
}
