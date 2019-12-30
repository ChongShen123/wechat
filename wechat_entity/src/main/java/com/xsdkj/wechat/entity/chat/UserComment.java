package com.xsdkj.wechat.entity.chat;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserComment implements Serializable {
    private Integer id;

    private Integer uid;

    private String nickname;

    private String moodId;

    /**
     * 内容 评论内容最多64个字符
     */
    private String content;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}