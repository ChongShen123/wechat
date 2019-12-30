package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UserCommentDto;
import com.xsdkj.wechat.entity.chat.UserComment;

/**
 * @author Administrator
 */
public interface UserCommentService {
    void save(UserCommentDto userCommentDto);

    void delete(UserComment userComment);
}
