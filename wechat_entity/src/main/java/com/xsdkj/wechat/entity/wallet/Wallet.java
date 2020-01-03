package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/3 9:41
 */
@Data
public class Wallet implements Serializable {
    private Integer id;

    private Integer uid;

    private BigDecimal price;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}
