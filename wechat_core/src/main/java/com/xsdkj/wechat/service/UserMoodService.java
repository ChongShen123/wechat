package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.entity.chat.UserMood;

/**
 * @author Administrator
 */
public interface UserMoodService {
    /**
     * 发一条朋友圈
     * @param moodDto moodDto
     */
    void save(MoodParamDto moodDto);

    void delete(UserMood userMood);
}
