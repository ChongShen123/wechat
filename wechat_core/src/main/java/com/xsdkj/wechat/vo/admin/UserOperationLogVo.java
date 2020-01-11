package com.xsdkj.wechat.vo.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tiankong
 * @date 2020/1/11 14:58
 */
@Data
public class UserOperationLogVo {
    private Integer id;
    private String username;
    private Integer operationId;
    private Integer uid;
    private BigDecimal price;
    private BigDecimal beforPrice;
    private BigDecimal afterPrice;
    private Byte type;
    private String explain;
    private Long createTimes;

    public String getCreateTimes() {
        if (createTimes != null) {
            return DateUtil.formatDateTime(new Date(createTimes));
        }
        return null;
    }

    public String getPrice() {
        if (price != null) {
            return "¥ " + NumberUtil.decimalFormatMoney(price.doubleValue());
        }
        return null;
    }

    public String getBeforPrice() {
        if (beforPrice != null) {
            return "¥ " + NumberUtil.decimalFormatMoney(price.doubleValue());
        }
        return null;
    }

    public String getAfterPrice() {
        if (afterPrice != null) {
            return "¥ " + NumberUtil.decimalFormatMoney(price.doubleValue());
        }
        return null;
    }
}
