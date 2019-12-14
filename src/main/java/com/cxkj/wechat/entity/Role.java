package com.cxkj.wechat.entity;

import java.io.Serializable;
import lombok.Data;

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