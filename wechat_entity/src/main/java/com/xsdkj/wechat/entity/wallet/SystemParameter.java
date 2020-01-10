package com.xsdkj.wechat.entity.wallet;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/6 10:38
 */
@Data
public class SystemParameter implements Serializable {
    private Integer id;

    /**
     * 最大补签次数限制:当达到这个次数后系统将不再送补签次数
     */
    private Integer maxRetroactiveCount;

    /**
     * 补签允许的几天限制.比如值为7则只可以补签7天之前的.
     */
    private Integer retroactiveTimeFrame;

    private static final long serialVersionUID = 1L;
}