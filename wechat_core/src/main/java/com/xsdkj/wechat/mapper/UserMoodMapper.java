package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.entity.mood.UserMood;

import com.xsdkj.wechat.entity.mood.UserThumbs;
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

    UserThumbs selectUserThumbsById(@Param("id") Integer id);

    UserComment selectUserCommentById(@Param("id")Integer id);

    /**
     * 查询好友朋友圈
     *
     * @param ids 好友id （包含自身id）
     * @return list
     */
    List<UserMoodVo> listUserMoodByUid(@Param("ids") List<Integer> ids);

}
