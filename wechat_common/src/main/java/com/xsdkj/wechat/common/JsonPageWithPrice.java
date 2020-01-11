package com.xsdkj.wechat.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author tiankong
 * @date 2020/1/11 14:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JsonPageWithPrice<T> extends JsonPage {
    private BigDecimal totalPrice;
}
