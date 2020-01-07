package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.PermissionBo;
import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.constant.RedisConstant;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.dto.UserLoginDto;
import com.xsdkj.wechat.dto.UserRegisterDto;
import com.xsdkj.wechat.dto.UserUpdateInfoParam;
import com.xsdkj.wechat.dto.UserUpdatePassword;
import com.xsdkj.wechat.entity.platform.Platform;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserLoginLog;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.mapper.UserLoginLogMapper;
import com.xsdkj.wechat.mapper.UserMapper;
import com.xsdkj.wechat.service.BaseService;
import com.xsdkj.wechat.service.PlatformService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.ex.ServiceException;
import com.xsdkj.wechat.util.*;
import com.xsdkj.wechat.vo.GroupVo;
import com.xsdkj.wechat.vo.LoginVo;
import com.xsdkj.wechat.vo.UserFriendVo;
import com.xsdkj.wechat.vo.admin.LoginInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
@Slf4j
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
    @Resource
    @Lazy
    private UserWalletService userWalletService;
    @Resource
    private PlatformService platformService;
    @Resource
    @Lazy
    private UserWalletService walletService;

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
        return userMapper.getOneByUsername(username);
    }

    @Override
    public void updateLoginState(Integer uid, Boolean type) {
        userMapper.updateLoginState(uid, type);
    }


    @Override
    public LoginVo register(UserRegisterDto param, HttpServletRequest request) {
        String passwordRegex = SystemConstant.PASSWORD_REGEX;
        User data = getByUsername(param.getUsername());
        if (data != null) {
            throw new ServiceException(ResultCodeEnum.USER_ALREADY_EXISTS);
        }
        Platform platform = platformService.getById(param.getPlatformId());
        if (platform == null) {
            throw new ServiceException(ResultCodeEnum.PLATFORM_NOT_FOUND);
        }
        if (!param.getPassword().matches(passwordRegex)) {
            throw new ServiceException(ResultCodeEnum.PASSWORD_FORMAT);
        }
        if (userMapper.countByEmail(param.getEmail()) > 0) {
            throw new ServiceException(ResultCodeEnum.EMAIL_ALREADY_EXISTS);
        }
        User user = getNewUser(param, request);
        userMapper.insert(user);
        Wallet newWallet = walletService.createNewWallet(user.getId());
        walletService.save(newWallet);
        UserLoginDto userLoginParam = new UserLoginDto(param.getUsername(), param.getPassword());
        return login(userLoginParam, request, false);
    }

    @Override
    public LoginVo login(UserLoginDto param, HttpServletRequest request, boolean check) {
        long begin = System.currentTimeMillis();
        log.debug(LogUtil.INTERVAL);
        log.debug("开始处理用户登录业务...");
        User user = userMapper.getOneByUsername(param.getUsername());
        if (check) {
            if (user == null) {
                throw new ServiceException(ResultCodeEnum.USER_NOT_FOND);
            }
            if (!encoder.matches(param.getPassword(), user.getPassword())) {
                throw new ServiceException(ResultCodeEnum.PASSWORD_NOT_MATCH);
            }
        }
        updateLoginState(user.getId(), UserConstant.LOGGED);
        user.setLoginState(UserConstant.LOGGED);
        log.debug("更新用户登录状态 {}ms", DateUtil.spendMs(begin));
        UserDetailsBo currentUserDetails = createCurrentUserDetailsBo(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        log.debug("保存用户信息到Security框架 {}ms", DateUtil.spendMs(begin));
        insertLoginLog(user, request);
        log.debug("保存用户登录日志 {}ms", DateUtil.spendMs(begin));
        //将用户信息保存到redis
        updateRedisDataByUid(user.getId(), currentUserDetails);
        log.debug("缓存用户数据至Redis {}ms", DateUtil.spendMs(begin));
        LoginVo loginVo = new LoginVo(user.getId(), user.getUsername(), user.getIcon(), tokenHead + " " + jwtTokenUtil.generateToken(user.getUsername() + ""), user.getQr(), user.getUno());
        log.debug("登录业务完毕用时 {}ms", DateUtil.spendMs(begin));
        return loginVo;
    }

    @Override
    public UserDetailsBo createCurrentUserDetailsBo(User user) {
        log.debug("创建一个UserDetailsBo业务对象");
        UserDetailsBo currentUserDetailsBo = new UserDetailsBo(user);
        List<PermissionBo> userPermission = getUserPermission(user.getId());
        log.debug("获取用户权限{}", userPermission);
        currentUserDetailsBo.setPermissionBos(userPermission);
        initUserGroup(user.getId(), currentUserDetailsBo, "createCurrentUserDetailsBo(User user) ");
        currentUserDetailsBo.setUserFriendVos(userMapper.listFriendByUserId(user.getId()));
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
        // TODO 登录日志放入队列
        userLoginLogMapper.insert(loginLog);
        User update = new User();
        update.setId(user.getId());
        update.setLastLoginTimes(System.currentTimeMillis());
        update.setLastLoginIp(ipAddress);
        // TODO 放入队列
        userMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    public List<User> listUserByIds(Set<Integer> ids) {
        return userMapper.listUserByIds(ids);
    }

    @Override
    public User getRedisUserByUserId(Integer uid) {
        UserDetailsBo bo;
        Object redisData = redisUtil.get(RedisConstant.REDIS_USER_ID + uid);
        if (redisData != null) {
            log.debug("从redis获取的用户{}缓存信息为:{}", uid, redisData);
            bo = JSONObject.toJavaObject(JSONObject.parseObject(redisData.toString()), UserDetailsBo.class);
            return bo.getUser();
        }
        log.debug("用户{}redis缓存信息为空,准备从本地获取用户信息");
        User user = getUserById(uid, false);
        if (user != null) {
            log.debug("用户信息为:{}", user);
            boolean updateRedisStatus = redisUtil.set(RedisConstant.REDIS_USER_ID + uid, JSONObject.toJSONString(uid), RedisConstant.REDIS_USER_TIMEOUT);
            log.debug("是否更新redis用户缓存:{}", updateRedisStatus);
            return user;
        }
        log.error("本地数据库未找到用户{}的相关信息,返回null", uid);
        return null;
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
        user.setPlatformId(param.getPlatformId());
        user.setNickname(param.getUsername());
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
    public List<UserFriendVo> listFriendByUid(Integer uid) {
        UserDetailsBo redisDataByUid = getRedisDataByUid(uid);
        return redisDataByUid.getUserFriendVos();
    }


    @Override
    public void deleteFriend(Integer uid, Integer friendId) {
        userMapper.deleteFriend(uid, friendId);
        userMapper.deleteFriend(friendId, uid);
        // TODO 更新下redis好友列表
    }


    /**
     * 初始化用户群组
     *
     * @param uid                  用户id
     * @param currentUserDetailsBo currentUserDetailsBo
     */
    private void initUserGroup(Integer uid, UserDetailsBo currentUserDetailsBo, String methodName) {
        long begin = System.currentTimeMillis();
        log.debug("初始化用户群组:{}", methodName);
        List<GroupVo> groupInfoBos = groupService.listGroupByUid(uid);
        if (groupInfoBos.size() > 0) {
            log.debug("用户群组:{}", groupInfoBos);
            for (GroupVo groupInfoBo : groupInfoBos) {
                currentUserDetailsBo.getUserGroupRelationMap().put(groupInfoBo.getGid(), groupInfoBo);
            }
            log.debug("用户群组初始化完毕,用户群组Map元素个数为:{} {}ms", currentUserDetailsBo.getUserGroupRelationMap().size(), DateUtil.spendMs(begin));
            return;
        }
        log.debug("用户群组为空");
    }

    @Override
    public UserDetailsBo updateRedisDataByUid(Integer uid, Wallet wallet) {
        log.debug("更新用户缓存 updateRedisDataByUid(Integer uid, Wallet wallet)  调用方:{}", Thread.currentThread().getStackTrace()[2].getMethodName());
        User user = userMapper.selectByPrimaryKey(uid);
        if (ObjectUtil.isNull(user)) {
            throw new NullPointerException();
        }
        UserDetailsBo currentUserDetailsBo = new UserDetailsBo(user);
        currentUserDetailsBo.setPermissionBos(getUserPermission(user.getId()));
        initUserGroup(uid, currentUserDetailsBo, "updateRedisDataByUid(Integer uid, Wallet wallet)");
        currentUserDetailsBo.setUserFriendVos(userMapper.listFriendByUserId(uid));
        currentUserDetailsBo.setWallet(wallet);
        redisUtil.set(RedisConstant.REDIS_USER_ID + uid, JSONObject.toJSONString(currentUserDetailsBo), RedisConstant.REDIS_USER_TIMEOUT);
        return currentUserDetailsBo;
    }

    @Override
    public UserDetailsBo updateRedisDataByUid(Integer uid, String methodName) {
        long begin = System.currentTimeMillis();
        log.debug("更新用户缓存updateRedisDataByUid(Integer uid, String methodName) 调用方:{}", methodName);
        User user = userMapper.selectByPrimaryKey(uid);
        if (ObjectUtil.isNull(user)) {
            throw new NullPointerException();
        }
        UserDetailsBo userDetailsBo = updateRedisDataByUid(user, "updateRedisDataByUid(Integer uid)");
        log.debug("用户缓存更新完成:{}ms", DateUtil.spendMs(begin));
        return userDetailsBo;
    }

    @Override
    public UserDetailsBo updateRedisDataByUid(User user, String methodName) {
        long begin = System.currentTimeMillis();
        log.debug("更新用户缓存 updateRedisDataByUid(User user, String methodName) 调用方:{}", methodName);
        UserDetailsBo currentUserDetailsBo = new UserDetailsBo(user);
        currentUserDetailsBo.setPermissionBos(getUserPermission(user.getId()));
        currentUserDetailsBo.setUserFriendVos(userMapper.listFriendByUserId(user.getId()));
        initUserGroup(user.getId(), currentUserDetailsBo, "updateRedisDataByUid(User user) ");
        redisUtil.set(RedisConstant.REDIS_USER_ID + user.getId(), JSONObject.toJSONString(currentUserDetailsBo), RedisConstant.REDIS_USER_TIMEOUT);
        log.debug("更新用户缓存完毕 {}ms", DateUtil.spendMs(begin));
        return currentUserDetailsBo;
    }

    @Override
    public UserDetailsBo updateRedisDataByUid(Integer uid, UserDetailsBo userDetailsBo) {
        String value = JSONObject.toJSONString(userDetailsBo);
        redisUtil.set(RedisConstant.REDIS_USER_ID + uid, value, RedisConstant.REDIS_USER_TIMEOUT);
        return userDetailsBo;
    }

    @Override
    public UserDetailsBo getRedisDataByUid(Integer uid) {
        Object redisData = redisUtil.get(RedisConstant.REDIS_USER_ID + uid);
        log.debug("用户{}缓存信息:{}", uid, redisData);
        UserDetailsBo bo;
        if (redisData != null) {
            JSONObject json = JSONObject.parseObject(redisData.toString());
            bo = JSONObject.toJavaObject(json, UserDetailsBo.class);
            return bo;
        }
        log.debug("用户{}缓存信息为空,准备从本地获取用户信息");
        User user = userMapper.selectByPrimaryKey(uid);
        if (user != null) {
            log.debug("用户信息为:{}", user);
            UserDetailsBo currentUserDetailsBo = createCurrentUserDetailsBo(user);
            boolean updateRedisStatus = redisUtil.set(RedisConstant.REDIS_USER_ID + uid, JSONObject.toJSONString(uid), RedisConstant.REDIS_USER_TIMEOUT);
            log.debug("已更新用户缓存:{}", updateRedisStatus);
            return currentUserDetailsBo;
        }
        log.error("本地未找到用户{}相关信息,返回null", uid);
        return null;
    }

    @Override
    public GroupVo getUserRedisGroup(Integer uid, Integer gid) {
        Map<Integer, GroupVo> userGroupRelationMap = getRedisDataByUid(uid).getUserGroupRelationMap();
        return userGroupRelationMap.get(gid);
    }

    @Override
    public User getUserById(Integer userId, boolean fromRedis) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (fromRedis) {
            updateRedisDataByUid(user, "getUserById(Integer userId, boolean fromRedis)");
        }
        return user;
    }

    @Override
    public int countUserIds(Set<Integer> userIds) {
        return userMapper.countUserIds(userIds);
    }
}
