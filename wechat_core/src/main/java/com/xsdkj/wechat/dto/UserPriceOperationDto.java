package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author tiankong
 * @date 2020/1/3 10:33
 */
@Data
public class UserPriceOperationDto {
    @NotNull(message = "用户id不能为空")
    private Integer uid;
    @NotNull(message = "充值类型不能为空")
    private Integer type;
    @NotNull(message = "充值金额不能为空")
    private BigDecimal price;
    @NotNull(message = "充值说明不能为空")
    private String explain;
}
