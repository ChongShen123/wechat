package com.xsdkj.wechat.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tiankong
 * @date 2020/1/6 15:10
 */
@Data
public class UserSignDateVo implements Serializable {
    private static final long serialVersionUID = 8933005904184502552L;
    private Integer id;
    private Integer platformId;
    private Integer year;
    private Integer month;
    private Date day;
    private Integer count;
    private Long createTimes;

    public String getDay() {
        if (day == null) {
            return null;
        }
        return DateUtil.formatDate(day);
    }

    public String getCreateTimes() {
        if (createTimes == null) {
            return null;
        }
        return DateUtil.formatDateTime(new Date(createTimes));
    }
}
