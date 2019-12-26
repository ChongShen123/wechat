package com.xsdkj.wechat.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.PermissionBo;
import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.dto.UserLoginDto;
import com.xsdkj.wechat.dto.UserRegisterDto;
import com.xsdkj.wechat.dto.UserUpdateInfoParam;
import com.xsdkj.wechat.dto.UserUpdatePassword;
import com.xsdkj.wechat.entity.chat.User;
import com.xsdkj.wechat.entity.chat.UserLoginLog;
import com.xsdkj.wechat.mapper.UserLoginLogMapper;
import com.xsdkj.wechat.mapper.UserMapper;
import com.xsdkj.wechat.service.BaseService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.ex.ServiceException;
import com.xsdkj.wechat.util.IpUtil;
import com.xsdkj.wechat.util.JwtTokenUtil;
import com.xsdkj.wechat.util.QrUtil;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.ListUserFriendVo;
import com.xsdkj.wechat.vo.LoginVo;
import com.xsdkj.wechat.vo.admin.LoginInfoVo;
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
        // TODO 更新Redis缓存
        //获取当前的用户信息
        UserDetailsBo userDetailsBo = userUtil.currentUser();
        User user = userDetailsBo.getUser();
        //获取当前用户传过来的老密码，并对密码进行比对返回一个boolean的值
        String passWord = password.getPassWord();
        boolean matches = encoder.matches(passWord, user.getPassword());
        //如果返回值是true，就把新密码插入
        if (matches) {
            user.setPassword(password.getNewPassWord());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public User getByUsername(String username) {
        Object jsonData = redisUtil.get(SystemConstant.REDIS_USER_KEY + username);
        UserDetailsBo userDetailsBo;
        if (jsonData == null) {
            User user = userMapper.getOneByUsername(username);
            if (user == null) {
                return null;
            }
            userDetailsBo = createCurrentUserDetailsBo(user);
            redisUtil.set(SystemConstant.REDIS_USER_KEY + user.getUsername(), JSONObject.toJSONString(userDetailsBo), SystemConstant.REDIS_USER_TIMEOUT);
        } else {
            userDetailsBo = JSONObject.toJavaObject(JSONObject.parseObject(jsonData.toString()), UserDetailsBo.class);
        }
        return userDetailsBo.getUser();
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
        UserDetailsBo currentUserDetails = createCurrentUserDetailsBo(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        insertLoginLog(user, request);
        //将用户信息保存到redis
        redisUtil.set(SystemConstant.REDIS_USER_KEY + user.getUsername(), JSONObject.toJSONString(currentUserDetails), SystemConstant.REDIS_USER_TIMEOUT);
        return new LoginVo(user.getId(), user.getUsername(), user.getIcon(), tokenHead + " " + jwtTokenUtil.generateToken(user.getUsername() + ""), user.getQr(), user.getUno());
    }

    @Override
    public UserDetailsBo createCurrentUserDetailsBo(User user) {
        // TODO 用户登录后将其所有好友也缓存到redis
        UserDetailsBo currentUserDetailsBo = new UserDetailsBo(user);
        currentUserDetailsBo.setPermissionBos(getUserPermission(user.getId()));
        currentUserDetailsBo.setGroupInfoBos(groupService.listGroupByUid(user.getId()));
        return currentUserDetailsBo;
    }

    @Override
    public List<PermissionBo> getUserPermission(Integer id) {
        List<PermissionBo> permissionList = userMapper.listPermissionByUid(id);
        return getPermissionTree(0, permissionList);
    }


    /**
     * 递归获取菜单权限树
     *
     * @param pid         权限父ID
     * @param permissions 子菜单
     * @return 返回所有子菜单父ID 为 pid的列表
     */
    private List<PermissionBo> getPermissionTree(Integer pid, List<PermissionBo> permissions) {
        List<PermissionBo> childList = new ArrayList<>();
        permissions.forEach(permissionBo -> {
            if (permissionBo.getPid().equals(pid)) {
                childList.add(permissionBo);
            }
        });
        childList.forEach(permissionBo -> permissionBo.setChildren(getPermissionTree(permissionBo.getId(), permissions)));
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
        String passwordRegex = SystemConstant.PASSWORD_REGEX;
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
        // TODO 需要更新Redis缓存 修改用户信息之前要判断该用户是否存在.
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
        user.setUno(generateUno());
        return user;
    }

    /**
     * 生成Uno
     *
     * @return Long
     */
    private Long generateUno() {
        Long uno;
        do {
            uno = Long.parseLong(NumberUtil.generateRandomNumber(SystemConstant.UNO_MIN, SystemConstant.UNO_MAX, 1)[0] + "");
        } while (userMapper.countByUno(uno) > 0);
        return uno;
    }

    @Override
    public LoginInfoVo getInfo() {
        UserDetailsBo user = userUtil.currentUser();
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setAvatar(user.getUser().getIcon());
        loginInfoVo.setName(user.getUsername());
        loginInfoVo.setMenus(user.getPermissionBos());
        return loginInfoVo;
    }

    @Override
    public List<ListUserFriendVo> listFriendByUserId(Integer uid) {
        List<ListUserFriendVo> listUserFriendVos = (List<ListUserFriendVo>) redisUtil.get(SystemConstant.REDIS_USER_FRIEND + uid);
        if (listUserFriendVos == null) {
            listUserFriendVos = updateRedisListFriendByUserId(uid);
        }
        return listUserFriendVos;
    }

    @Override
    public List<ListUserFriendVo> updateRedisListFriendByUserId(Integer uid) {
        List<ListUserFriendVo> listUserFriendVos = userMapper.listFriendByUserId(uid);
        redisUtil.set(SystemConstant.REDIS_USER_FRIEND + uid, listUserFriendVos, SystemConstant.REDIS_USER_TIMEOUT);
        return listUserFriendVos;
    }

    @Override
    public void deleteFriend(Integer uid, Integer friendId) {
        userMapper.deleteFriend(uid, friendId);
        // 更新下redis好友列表
        updateRedisListFriendByUserId(uid);
    }
}