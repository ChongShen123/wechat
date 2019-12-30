package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.chat.UserComment;

public interface UserCommentService {
    void save(UserComment userComment);
    void delete(UserComment userComment);
}
