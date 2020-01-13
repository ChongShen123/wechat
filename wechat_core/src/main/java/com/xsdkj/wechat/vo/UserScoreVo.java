package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2020/1/6 15:29
 */
@Data
public class UserScoreVo implements Serializable {
    private static final long serialVersionUID = 2433258809819038323L;
    private Integer id;

    private Integer uid;
    private String username;
    /**
     * 积分
     */
    private Integer score;

    /**
     * 补签次数
     */
    private Integer retroactiveCount;

    /**
     * 连接签到天数
     */
    private Integer successionCount;

    /**
     * 上次签到时间
     */
    private Long lastSignDateTimes;
}
