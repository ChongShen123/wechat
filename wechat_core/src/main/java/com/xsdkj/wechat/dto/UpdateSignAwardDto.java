package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2020/1/14 16:01
 */
@Data
public class UpdateSignAwardDto {
    @NotNull(message = "ID不能为空")
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
