package com.cxkj.wechat.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
@Data
public class Permission implements Serializable {
    private Integer id;

    private Integer pid;

    private String value;

    private String icon;

    /**
    * 权限类型: 0目录 1菜单 2按钮 3 接口
    */
    private Byte type;

    /**
    * 前端资源路径
    */
    private String uri;

    /**
    * 启用状态：0禁用 1启用
    */
    private Byte status;

    private Long createTimes;

    private Integer sort;

    private static final long serialVersionUID = 1L;
}