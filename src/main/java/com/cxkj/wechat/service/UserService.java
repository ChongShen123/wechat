package com.cxkj.wechat.service;

import com.cxkj.wechat.vo.LoginVo;
import com.cxkj.wechat.dto.UserLoginDto;
import com.cxkj.wechat.dto.UserRegisterDto;
import com.cxkj.wechat.entity.User;

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
     * @param param 登录参数
     * @return 登录信息
     */
    LoginVo login(UserLoginDto param, HttpServletRequest request, boolean check);

    /**
     * 用户注册
     *
     * @param param 注册参数
     * @return 登录信息
     */
    LoginVo register(UserRegisterDto param, HttpServletRequest request);

    /**
     * 查询用户
     * @param ids ids
     * @return List
     */
    List<User> listUserByIds(Set<Integer> ids);

    /**
     * 查询用户
     * @param id id
     * @return 用户
     */
    User getByUserId(Integer id);
}
