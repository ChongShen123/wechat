package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/5 11:22
 */
@Data
public class SignAward implements Serializable {
    private Short id;

    /**
    * 奖励积分
    */
    private Integer awardScore;

    /**
    * 连接签到天数
    */
    private Integer successionCount;

    private static final long serialVersionUID = 1L;
}