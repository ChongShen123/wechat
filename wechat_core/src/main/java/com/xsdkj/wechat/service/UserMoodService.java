package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.MoodParamDto;
<<<<<<< HEAD

import com.xsdkj.wechat.vo.UserMoodVo;

import java.util.List;

import com.xsdkj.wechat.entity.mood.UserMood;

=======
import com.xsdkj.wechat.vo.UserMoodVo;

import java.util.List;
import com.xsdkj.wechat.entity.mood.UserMood;
>>>>>>> 27edae3208a992a05a995390701c4070e0a6af6c

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
