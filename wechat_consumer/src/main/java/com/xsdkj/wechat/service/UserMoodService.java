package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.mood.UserMood;

/**
 * @author Administrator
 */
public interface UserMoodService {
    void save(UserMood mood);

    void delete(UserMood mood);


}
