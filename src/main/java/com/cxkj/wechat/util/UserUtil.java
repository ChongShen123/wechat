package com.cxkj.wechat.util;

import com.cxkj.wechat.bo.CurrentUserDetailsBo;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/11 13:05
 */
@Component
public class UserUtil {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    public CurrentUserDetailsBo currentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        CurrentUserDetailsBo bo = (CurrentUserDetailsBo) redisUtil.get(SystemConstant.REDIS_USER_KEY + currentUsername);
        if (bo == null) {
            User user = userService.getByUsername(currentUsername);
            CurrentUserDetailsBo currentUserDetailsBo = new CurrentUserDetailsBo(user);
            currentUserDetailsBo.setPermissionBos(userService.getUserPermission(user.getId()));
        }

        return bo;
    }

}
