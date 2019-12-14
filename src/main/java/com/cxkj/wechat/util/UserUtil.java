package com.cxkj.wechat.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.service.cache.UserCache;
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
    private UserCache userCache;

    public User currentUser() {
        return userCache.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
