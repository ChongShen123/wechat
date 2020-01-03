package com.xsdkj.wechat.entity.chat;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/2 17:50
 */
@Data
public class Platform implements Serializable {
    private Integer id;

    private String name;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}