package com.cxkj.wechat.util;

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

    public User currentUser() {
        return userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
