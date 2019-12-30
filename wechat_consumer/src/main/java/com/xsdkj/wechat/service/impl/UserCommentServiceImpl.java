package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.UserComment;
import com.xsdkj.wechat.mapper.UserCommentMapper;
import com.xsdkj.wechat.service.UserCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class UserCommentServiceImpl implements UserCommentService {
    @Resource
    UserCommentMapper userCommentMapper;
    @Override
    public void save(UserComment userComment) {
        userCommentMapper.insertSelective(userComment);
    }

    @Override
    public void delete(UserComment userComment) {
        userCommentMapper.deleteByPrimaryKey(userComment.getId());
    }
}
