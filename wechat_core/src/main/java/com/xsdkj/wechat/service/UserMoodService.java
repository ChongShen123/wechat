package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.entity.chat.UserMood;
import com.xsdkj.wechat.vo.UserMoodVo;

import java.util.List;

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


    void selectAll( );

    /**
     * 查询朋友圈的所有好友动态
     * @param uid
     * @return
     */
    List<UserMoodVo> listUserMoodByUid(Integer uid);
}
