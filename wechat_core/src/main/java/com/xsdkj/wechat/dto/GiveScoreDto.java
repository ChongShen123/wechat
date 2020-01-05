package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2020/1/5 17:51
 */
@Data
public class GiveScoreDto {
    @NotNull(message = "用户id不能为空")
    private String userIds;
    @NotNull(message = "赠送积分不能为空")
    private Integer score;
}
