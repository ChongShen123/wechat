package com.xsdkj.wechat.mapper;


import com.xsdkj.wechat.entity.chat.UserMood;

/**
 * @author Administrator
 */
public interface UserMoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMood record);

    int insertSelective(UserMood record);

    UserMood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMood record);

    int updateByPrimaryKey(UserMood record);


}