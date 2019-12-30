package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.UserMood;
import com.xsdkj.wechat.mapper.UserMoodMapper;
import com.xsdkj.wechat.service.UserMoodService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class UserMoodServiceImpl implements UserMoodService {
    @Resource
    private UserMoodMapper userMoodMapper;
    @Override
    public void save(UserMood mood) {
        System.out.println(mood);
        userMoodMapper.insert(mood);
    }

    @Override
    public void delete(UserMood mood) {
        userMoodMapper.deleteByPrimaryKey(mood.getId());
    }
}
