package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 送补签次数
 *
 * @author tiankong
 * @date 2020/1/5 15:53
 */
@Data
public class GiveRetroactiveCountDto {
    @NotNull(message = "用户ID不能为空")
    private String userIds;
    @NotNull(message = "签到次数不能为空")
    private Integer count;
}
