package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/6 15:01
 */
@Data
public class SignDate implements Serializable {
    private Integer id;

    /**
     * 平台id 每个平台签到不一样所以要分开
     */
    private Integer platformId;

    private Integer year;

    private Integer month;

    /**
     * 日期
     */
    private Date day;

    /**
     * 签到人数
     */
    private Integer count;

    private static final long serialVersionUID = 1L;
}