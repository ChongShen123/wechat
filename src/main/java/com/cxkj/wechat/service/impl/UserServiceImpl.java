package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.bo.CurrentUserDetails;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.dto.LoginInfo;
import com.cxkj.wechat.dto.UserLoginParam;
import com.cxkj.wechat.dto.UserRegisterParam;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.entity.UserLoginLog;
import com.cxkj.wechat.mapper.UserLoginLogMapper;
import com.cxkj.wechat.mapper.UserMapper;
import com.cxkj.wechat.service.cache.UserCache;
import com.cxkj.wechat.service.UserService;
import com.cxkj.wechat.service.ex.ServiceException;
import com.cxkj.wechat.util.IpUtil;
import com.cxkj.wechat.util.JwtTokenUtil;
import com.cxkj.wechat.util.QrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

/**
 * @author tiankong
 * @date 2019/12/11 13:12
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserCache userCache;
    @Resource
    private PasswordEncoder encoder;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserLoginLogMapper userLoginLogMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${file.img-path}")
    private String imgPath;
    @Resource
    private QrUtil qrUtil;

    @Override
    public User getByUsername(String username) {
        return userCache.getByUsername(username);
    }

    @Override
    public LoginInfo login(UserLoginParam param, HttpServletRequest request, boolean check) {
        System.out.println(param);
        User user = userMapper.getOneByUsername(param.getUsername());
        if (check) {
            if (user == null) {
                throw new ServiceException(ResultCodeEnum.USER_NOT_FOND);
            }
            if (!encoder.matches(param.getPassword(), user.getPassword())) {
                throw new ServiceException(ResultCodeEnum.PASSWORD_NOT_MATCH);
            }
        }
        CurrentUserDetails currentUserDetails = new CurrentUserDetails(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        System.out.println(user);
        insertLoginLog(user, request);
        return new LoginInfo(user.getId(), user.getUsername(), user.getIcon(), tokenHead + " " + jwtTokenUtil.generateToken(user.getUsername()), user.getQr());
    }

    private void insertLoginLog(User user, HttpServletRequest request) {
        String ipAddress = IpUtil.getIpAddress(request);
        UserLoginLog loginLog = new UserLoginLog(user, ipAddress);
        userLoginLogMapper.insert(loginLog);
        User update = new User();
        update.setId(user.getId());
        update.setLastLoginTimes(System.currentTimeMillis());
        update.setLastLoginIp(ipAddress);
        userMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    public List<User> listUserByIds(List<Integer> ids) {
        return userMapper.listUserByIds(ids);
    }

    @Override
    public LoginInfo register(UserRegisterParam param, HttpServletRequest request) {
        String passwordRegex = "^[0-9A-Za-z_]{6,12}$";
//        String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        User data = getByUsername(param.getUsername());
        if (data != null) {
            throw new ServiceException(ResultCodeEnum.USER_ALREADY_EXISTS);
        }

        if (!param.getPassword().matches(passwordRegex)) {
            throw new ServiceException(ResultCodeEnum.PASSWORD_FORMAT);
        }
        if (userMapper.countByEmail(param.getEmail()) > 0) {
            throw new ServiceException(ResultCodeEnum.EMAIL_ALREADY_EXISTS);
        }
        User user = getNewUser(param, request);
        userMapper.insert(user);
        UserLoginParam userLoginParam = new UserLoginParam(param.getUsername(), param.getPassword());
        return login(userLoginParam, request, false);
    }

    private User getNewUser(UserRegisterParam param, HttpServletRequest request) {
        User user = new User();
        user.setUsername(param.getUsername());
        user.setPassword(encoder.encode(param.getPassword()));
        user.setIcon(imgPath + "default" + new Random().nextInt(8) + ".png");
        user.setGender((byte) 0);
        user.setQr(qrUtil.generate(user.getUsername()));
        user.setEmail(param.getEmail());
        user.setType((byte) 0);
        user.setState((byte) 0);
        user.setCreateTimes(System.currentTimeMillis());
        user.setJoinIp(IpUtil.getIpAddress(request));
        return user;
    }
}
