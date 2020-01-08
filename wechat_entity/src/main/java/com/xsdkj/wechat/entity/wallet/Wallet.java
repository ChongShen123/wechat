package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/4 18:08
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
    /**
     * 支付密码
     */
    private String password;
    private Long createTimes;
    /**
     * 更新时间
     */
    private Long modifiedTimes;
    /**
     * 0未设置支付密码 1已设置支付密码
     */
    private Byte state;
    private static final long serialVersionUID = 1L;
}