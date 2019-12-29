package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.dto.UserThumbsDto;
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

    /**
     * 保存
     * @param userMood
     */
    void delete(UserMood userMood);

    void saveThumbs(UserThumbsDto userThumbsDto);
}
