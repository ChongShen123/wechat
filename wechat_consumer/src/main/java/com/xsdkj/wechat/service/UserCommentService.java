package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.mood.UserComment;

public interface UserCommentService {
    void save(UserComment userComment);
    void delete(UserComment userComment);
}
