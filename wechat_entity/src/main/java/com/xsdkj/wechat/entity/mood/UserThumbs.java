package com.xsdkj.wechat.entity.mood;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserThumbs implements Serializable {
    private Integer id;

    private Integer uid;

    private String nickname;

    /**
     * 心静id
     */
    private Integer moodId;

    private static final long serialVersionUID = 1L;
}
