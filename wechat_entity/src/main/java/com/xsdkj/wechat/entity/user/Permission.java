package com.xsdkj.wechat.entity.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/23 13:46
 */
@Data
public class Permission implements Serializable {
    protected Integer id;

    protected Integer pid;

    protected String name;

    protected String path;

    /**
     * 前端资源路径
     */
    protected String component;

    protected String title;

    protected String icon;

    protected String redirect;

    /**
     * 权限类型: 0目录 1菜单 2按钮 3 接口
     */
    protected Byte type;

    protected Boolean hidden;

    /**
     * 启用状态：0禁用 1启用
     */
    protected Byte status;

    protected Integer sort;

    protected Long createTimes;

    protected static final long serialVersionUID = 1L;
}
