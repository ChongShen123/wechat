package com.xsdkj.wechat.entity.user;

import java.io.Serializable;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/30 17:26
 */
@Data
public class User implements Serializable {
    protected Integer id;

    /**
     * 平台id
     */
    protected Integer platformId;

    /**
     * 系统账号
     */
    protected Long uno;

    protected String username;

    /**
     * 昵称
     */
    protected String nickname;

    protected String password;

    protected String icon;

    /**
     * 0男 1女
     */
    protected Byte gender;

    /**
     * 二维码
     */
    protected String qr;

    /**
     * 电话
     */
    protected String tel;

    protected String qq;

    protected String description;

    protected String email;

    /**
     * 0普通用户 1管理员
     */
    protected Byte type;

    /**
     * 状态 0 正常 1禁用 2 删除
     */
    protected Byte state;

    /**
     * 创建时间
     */
    protected Long createTimes;

    /**
     * 加入ip
     */
    protected String joinIp;

    /**
     * 上次登录时间
     */
    protected Long lastLoginTimes;

    /**
     * 上次登录ip
     */
    protected String lastLoginIp;

    /**
     * 登录状态 0未登录 1已登录
     */
    protected Boolean loginState;

    protected static final long serialVersionUID = 1L;
}
