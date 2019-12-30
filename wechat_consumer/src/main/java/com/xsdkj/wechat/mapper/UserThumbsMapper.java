package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.chat.UserThumbs;

/**
 * @author Administrator
 */
public interface UserThumbsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserThumbs record);

    int insertSelective(UserThumbs record);

    UserThumbs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserThumbs record);

    int updateByPrimaryKey(UserThumbs record);
}