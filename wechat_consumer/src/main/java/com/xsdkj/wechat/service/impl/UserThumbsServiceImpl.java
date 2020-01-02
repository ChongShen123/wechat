package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.UserThumbs;
import com.xsdkj.wechat.mapper.UserThumbsMapper;
import com.xsdkj.wechat.service.UserThumbsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class UserThumbsServiceImpl implements UserThumbsService {
    @Resource
    UserThumbsMapper userThumbsMapper;
    @Override
    public void save(UserThumbs thumbs) {
    userThumbsMapper.insert(thumbs);
    }
    @Override
    public void delete(UserThumbs userThumbs) {
        userThumbsMapper.deleteByPrimaryKey(userThumbs.getId());
    }


}
