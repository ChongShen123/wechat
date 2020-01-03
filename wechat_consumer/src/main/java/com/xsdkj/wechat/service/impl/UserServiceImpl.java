package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.mapper.UserMapper;
import com.xsdkj.wechat.mapper.UserOperationLogMapper;
import com.xsdkj.wechat.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author tiankong
 * @date 2020/1/2 10:31
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserOperationLogMapper userOperationLogMapper;

    @Override
    public void save(User user) {
        userMapper.insert(user);
    }

    @Override
    public void saveUserOperationLog(UserOperationLog userOperationLog) {
        userOperationLogMapper.insert(userOperationLog);
    }
}