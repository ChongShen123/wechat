package com.xsdkj.wechat.service;


import com.xsdkj.wechat.entity.mood.UserThumbs;

/**
 * @author Administrator
 */
public interface UserThumbsService {
    void save(UserThumbs thumbs);

    void delete(UserThumbs userThumbs);
}
