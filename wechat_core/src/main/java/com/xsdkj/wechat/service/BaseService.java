package com.xsdkj.wechat.service;

import com.xsdkj.wechat.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/24 14:48
 */
@Service
public abstract class BaseService {
    @Resource
    protected RedisUtil redisUtil;
    @Resource
    protected UserGroupService groupService;
}
