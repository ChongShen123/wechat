package com.xsdkj.wechat.vo;

import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/14 14:59
 */
@Data
public class SignAwardVo {

    private Short id;

    /**
     * 奖励积分
     */
    private Integer awardScore;

    /**
     * 连接签到天数
     */
    private Integer successionCount;
}
