package com.xsdkj.wechat.entity.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户评论表
 */
@Data
public class UserComment implements Serializable {
    Integer id;
    Integer uid;
    String nickname;
    String moodId;
    String content;
    Long createTimes;
    private static final long serialVersionUID = 1L;
}
