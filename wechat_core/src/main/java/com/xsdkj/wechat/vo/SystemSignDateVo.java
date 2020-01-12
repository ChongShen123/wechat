package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2020/1/12 18:23
 */
@Data
public class SystemSignDateVo implements Serializable {
    private static final long serialVersionUID = 7928459053902836470L;
    private Integer retroactiveTimeFrame;
    private Integer maxRetroactiveCount;
}
