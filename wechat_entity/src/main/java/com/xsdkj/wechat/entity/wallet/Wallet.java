package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/3 12:43
 */
@Data
public class Wallet implements Serializable {
    private Integer id;

    private Integer uid;

    private BigDecimal price;

    /**
     * 总充值
     */
    private BigDecimal totalPrice;

    private Long createTimes;

    /**
     * 更新时间
     */
    private Long modifiedTimes;

    private static final long serialVersionUID = 1L;
}