package com.xsdkj.wechat.util;

import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.constant.RedisConstant;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 该类可获取当前登录的用户信息
 *
 * @author tiankong
 * @date 2019/12/11 13:05
 */
@Slf4j
@Component
public class UserUtil {

    /**
     * 这里注入UserService 违反了 单一职责原则.
     * 由于此类比较特殊所以还是得注入下.
     * 加@Lazy是因为在 UserService中也注入了 UserUtil.
     * 程序启动时这里的UserService让容器先不创建,防止循环依赖注入.
     * 如果不加启动程序就会报循环依赖错误,而无法正常启动项目.
     */
    @Resource
    @Lazy
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    public UserDetailsBo currentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetailsBo bo = (UserDetailsBo) redisUtil.get(RedisConstant.REDIS_UID + currentUsername);
        if (bo == null) {
            bo = userService.updateRedisDataByUid(userService.getByUsername(currentUsername).getId(), "UserUtil.currentUser()");
        }
        return bo;
    }

    public User getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String name = authentication.getName();
        return userService.getRedisData(name);
    }
}
