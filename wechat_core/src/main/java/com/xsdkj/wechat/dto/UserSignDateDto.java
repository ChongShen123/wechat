package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2020/1/6 14:52
 */
@Data
public class UserSignDateDto {
    @NotNull(message = "年份不能为空")
    private Integer year;
    @NotNull(message = "月份不能为空")
    private Integer month;
    @NotNull(message = "用户id不能为空")
    private Integer uid;
}
