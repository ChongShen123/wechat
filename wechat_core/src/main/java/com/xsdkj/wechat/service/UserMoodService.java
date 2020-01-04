package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.MoodParamDto;

import com.xsdkj.wechat.vo.UserMoodVo;

import java.util.List;

import com.xsdkj.wechat.entity.mood.UserMood;


/**
 * @author Administrator
 */
public interface UserMoodService {
    /**
     * 发一条朋友圈
     * @param moodDto moodDto
     */
    void save(MoodParamDto moodDto);
    /**
     * 保存
     * @param userMood
     */
    void delete(UserMood userMood);




    /**
     * 查询朋友圈的所有好友动态
     * @return list
     */
    List<UserMoodVo> listUserMoodByUid();


}
