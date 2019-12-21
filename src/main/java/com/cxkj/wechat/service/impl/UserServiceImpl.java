package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.bo.CurrentUserDetailsBo;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.dto.UserUpdateInfoParam;
import com.cxkj.wechat.dto.UserUpdatePassword;
import com.cxkj.wechat.util.UserUtil;
import com.cxkj.wechat.vo.LoginVo;
import com.cxkj.wechat.dto.UserLoginDto;
import com.cxkj.wechat.dto.UserRegisterDto;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.entity.UserLoginLog;
import com.cxkj.wechat.mapper.UserLoginLogMapper;
import com.cxkj.wechat.mapper.UserMapper;
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
import java.util.Set;

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
    @Resource
    private UserUtil userUtil;

    /**
     * 修改密码
     * @param password
     */
    @Override
    public void updatePassWord(UserUpdatePassword password) {
        //获取当前的用户信息
       User user = userUtil.currentUser();
       //获取当前用户传过来的老密码，并对密码进行比对返回一个boolean的值
        String passWord = password.getPassWord();
        boolean matches = encoder.matches(passWord, user.getPassword());
        //如果返回值是true，就把新密码插入
        if(matches==true){
            user.setPassword(password.getNewPassWord());
            userMapper.updateByPrimaryKeySelective(user);
        }


//        User user=new User();
//        if(password.getPassWord()==null || password.getPassWord()!=user.getPassword()){
//            throw new ServiceException(ResultCodeEnum.PASSWORD_NOT_MATCH);
//        }
//        user.setId(password.getId());
//        user.setPassword(password.getNewPassWord());
//        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.getOneByUsername(username);
    }

    @Override
    public LoginVo login(UserLoginDto param, HttpServletRequest request, boolean check) {
        User user = userMapper.getOneByUsername(param.getUsername());
        if (check) {
            if (user == null) {
                throw new ServiceException(ResultCodeEnum.USER_NOT_FOND);
            }
            if (!encoder.matches(param.getPassword(), user.getPassword())) {
                throw new ServiceException(ResultCodeEnum.PASSWORD_NOT_MATCH);
            }
        }
        CurrentUserDetailsBo currentUserDetails = new CurrentUserDetailsBo(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        insertLoginLog(user, request);
        return new LoginVo(user.getId(), user.getUsername(), user.getIcon(), tokenHead + " " + jwtTokenUtil.generateToken(user.getUsername()), user.getQr());
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
    public List<User> listUserByIds(Set<Integer> ids) {
        return userMapper.listUserByIds(ids);
    }



    @Override
    public LoginVo register(UserRegisterDto param, HttpServletRequest request) {
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
        UserLoginDto userLoginParam = new UserLoginDto(param.getUsername(), param.getPassword());
        return login(userLoginParam, request, false);
    }

    @Override
    public User getByUserId(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
    /**
     * 修改用户的密码
     * @param param
     */
    @Override
    public void  updateUserInfo(UserUpdateInfoParam param) {
        String emailRegex=" ^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        User user=new User();
        user.setId(param.getId());
        user.setIcon(param.getIcon());
        if(!param.getEmail().matches(emailRegex)){
          throw  new ServiceException(ResultCodeEnum.EMAIL);
        }
        user.setEmail(param.getEmail());
           user.setTel(param.getTel());
        user.setQq(param.getQq());
        userMapper.updateByPrimaryKeySelective(user);
    }




    private User getNewUser(UserRegisterDto param, HttpServletRequest request) {
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
