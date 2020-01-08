package com.xsdkj.wechat.mapper;


import com.xsdkj.wechat.entity.mood.UserMood;


import com.xsdkj.wechat.vo.UserMoodVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMood record);

    int insertSelective(UserMood record);

    UserMood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMood record);

    int updateByPrimaryKey(UserMood record);


/*    UserThumbs selectUserThumbsById(@Param("id") Integer id);

    UserComment selectUserCommentById(@Param("id")Integer id);*/

    /*    *//**
     * 查询自己的动态
     * @param id
     * @return
     *//*
        List<UserMoodVo>  selectOneSelf(@Param("id") Integer id);*/

    /**
     * 查询好友朋友圈
     *
     * @param ids
     * @return list
     */
    List<UserMoodVo> listUserMoodByUid( @Param("ids") List<Integer> ids);

    /**
     * @author 查询所有包含自己的动态
     */
    List<UserMoodVo> listUserMoodAllByUid(@Param("id") Integer id);


    List<Integer> selectAllId();
}

