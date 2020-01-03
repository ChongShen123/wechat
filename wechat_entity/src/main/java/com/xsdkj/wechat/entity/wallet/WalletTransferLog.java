package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/3 9:42
 */
@Data
public class WalletTransferLog implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer toUserId;

    private BigDecimal price;

    private BigDecimal beforePrice;

    private BigDecimal afterPrice;

    /**
    * 月份
    */
    private Byte month;

    /**
    * 年
    */
    private Integer year;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}