package com.xsdkj.wechat.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author tiankong
 * @date 2020/1/14 12:44
 */
@Data
public class UserSignDateDetailVo {
    private Integer id;
    private Integer uid;
    private Date date;
    private Long createTimes;
    private Integer score;
    private String username;

    public String getDate() {
        if (date == null) {
            return null;
        }
        return DateUtil.formatDate(date);
    }

    public String getCreateTimes() {
        if (createTimes == null) {
            return null;
        }
        return DateUtil.formatTime(new Date(createTimes));
    }
}
