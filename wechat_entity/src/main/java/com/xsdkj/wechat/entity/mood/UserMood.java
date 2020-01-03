package com.xsdkj.wechat.entity.mood;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class UserMood implements Serializable {
    private Integer id;

    private Integer uid;

    /**
     * 正文
     */
    private String content;

    /**
     * 图片地址,用逗号隔开
     */
    private String file;

    /**
     * 0 无 1图片 2小视频
     */
    private Byte fileType;

    /**
     * 评论条数
     */
    private Integer commentCount;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}
