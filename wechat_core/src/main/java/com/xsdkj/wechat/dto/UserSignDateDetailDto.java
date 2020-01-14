package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2020/1/14 12:49
 */
@Data
public class UserSignDateDetailDto {
    @NotNull(message = "id不能为空")
    private Integer signDateId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
