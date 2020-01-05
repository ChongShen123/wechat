package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/5 12:45
 */
@Data
public class UserScore implements Serializable {
    private Integer id;

    private Integer uid;

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

    private static final long serialVersionUID = 1L;
}