package com.xsdkj.wechat.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tiankong
 * @date 2019/12/30 14:48
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = 3452773848565100595L;
    private Integer id;
    private Long uno;
    private String username;
    private String nickname;
    private String icon;
    private Byte gender;
    private String qr;
    private String email;
    private String tel;
    private String qq;
    private String description;
    private Byte type;
    private Long lastLoginTimes;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Long createTimes;
    private Boolean loginState;

    public String getLastLoginTimes() {
        if (lastLoginTimes != null) {
            Date date = new Date(lastLoginTimes);
            return DateUtil.formatDateTime(date);
        }
        return null;
    }

    public String getCreateTimes() {
        if (createTimes != null) {
            Date date = new Date(createTimes);
            return DateUtil.formatDateTime(date);
        }
        return null;
    }

    public String getPrice() {
        if (price != null) {
            return "¥ " + NumberUtil.decimalFormatMoney(price.doubleValue());
        }
        return null;
    }

    public String getTotalPrice() {
        if (price != null) {
            return "¥ " + NumberUtil.decimalFormatMoney(totalPrice.doubleValue());
        }
        return null;
    }
}
