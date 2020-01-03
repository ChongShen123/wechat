package com.xsdkj.wechat.entity.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
@Data
public class Role implements Serializable {
    private Integer id;

    private String name;

    private String description;

    private Byte status;

    private Integer sort;

    private Long createTimes;

    private Long modifiedTimes;

    private static final long serialVersionUID = 1L;
}
