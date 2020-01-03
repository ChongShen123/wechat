package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/3 18:17
 */
@Data
public class WalletPriceChangeLog implements Serializable {
    private Integer id;

    private Integer uid;

    private BigDecimal price;

    private BigDecimal beforePrice;

    private BigDecimal afterPrice;

    /**
     * 添加类型 0减少 1添加
     */
    private Boolean addType;

    /**
     * 1充值 2 提现 3 转账 4 红包
     */
    private Byte type;

    /**
     * 月份
     */
    private Byte month;

    /**
     * 年份
     */
    private Integer year;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}