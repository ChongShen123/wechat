package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.bo.CurrentUserDetailsBo;
import com.cxkj.wechat.bo.PermissionBo;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.dto.UserUpdateInfoParam;
import com.cxkj.wechat.dto.UserUpdatePassword;
import com.cxkj.wechat.service.BaseService;
import com.cxkj.wechat.util.UserUtil;
import com.cxkj.wechat.vo.ListGroupVo;
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
import com.cxkj.wechat.vo.admin.LoginInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author tiankong
 * @date 2019/12/11 13:12
 */
@Service
@Transactional
public class UserServiceImpl extends BaseService implements UserService {
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

    @Override
    public void updatePassword(UserUpdatePassword password) {
        //获取当前的用户信息
        CurrentUserDetailsBo userDetailsBo = userUtil.currentUser();
        User user = userDetailsBo.getUser();
        //获取当前用户传过来的老密码，并对密码进行比对返回一个boolean的值
        String passWord = password.getPassWord();
        boolean matches = encoder.matches(passWord, user.getPassword());
        //如果返回值是true，就把新密码插入
        if (matches) {
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

        CurrentUserDetailsBo currentUserDetails = createCurrentUserDetailsBo(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        insertLoginLog(user, request);
        //将用户信息保存到redis
        redisUtil.set(SystemConstant.REDIS_USER_KEY + user.getUsername(), currentUserDetails, SystemConstant.REDIS_USER_TIMEOUT);
        return new LoginVo(user.getId(), user.getUsername(), user.getIcon(), tokenHead + " " + jwtTokenUtil.generateToken(user.getUsername()), user.getQr());
    }

    @Override
    public CurrentUserDetailsBo createCurrentUserDetailsBo(User user) {
        CurrentUserDetailsBo currentUserDetailsBo = new CurrentUserDetailsBo(user);
        currentUserDetailsBo.setPermissionBos(getUserPermission(user.getId()));
        currentUserDetailsBo.setGroupInfoBos(groupService.listGroupByUid(user.getId()));
        return currentUserDetailsBo;
    }

    @Override
    public List<PermissionBo> getUserPermission(Integer id) {
        List<PermissionBo> permissionList = userMapper.listPermissionByUid(id);
        return classify(0, permissionList);
    }

    private List<PermissionBo> classify(Integer pid, List<PermissionBo> permissions) {
        List<PermissionBo> childList = new ArrayList<>();
        permissions.forEach(permissionBo -> {
            if (permissionBo.getPid().equals(pid)) {
                childList.add(permissionBo);
            }
        });
        childList.forEach(permissionBo -> permissionBo.setChildren(classify(permissionBo.getId(), permissions)));
        childList.sort(order());
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }


    /**
     * 根据 sort进行排序
     *
     * @return Comparator
     */
    private Comparator<PermissionBo> order() {
        return Comparator.comparingInt(PermissionBo::getSort);
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

    @Override
    public void updateUserInfo(UserUpdateInfoParam param) {
        String emailRegex = SystemConstant.EMAIL_REGEX;
        User user = new User();
        user.setId(param.getId());
        user.setIcon(param.getIcon());
        if (!param.getEmail().matches(emailRegex)) {
            throw new ServiceException(ResultCodeEnum.EMAIL);
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

    @Override
    public LoginInfoVo getInfo() {
        CurrentUserDetailsBo user = userUtil.currentUser();
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setAvatar(user.getUser().getIcon());
        loginInfoVo.setName(user.getUsername());
        loginInfoVo.setMenus(user.getPermissionBos());
        return loginInfoVo;
    }

}
