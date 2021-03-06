package com.cxkj.wechat.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/11 11:38
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String icon;

    /**
     * 0男 1女
     */
    private Byte gender;

    /**
     * 二维码
     */
    private String qr;

    /**
     * 电话
     */
    private String tel;

    private String qq;

    private String description;

    private String email;

    /**
     * 0普通用户 1管理员
     */
    private Byte type;

    /**
     * 状态 0 正常 1禁用 2 删除
     */
    private Byte state;

    /**
     * 创建时间
     */
    private Long createTimes;

    /**
     * 加入ip
     */
    private String joinIp;

    /**
     * 上次登录时间
     */
    private Long lastLoginTimes;

    /**
     * 上次登录ip
     */
    private String lastLoginIp;

    private static final long serialVersionUID = 1L;

    public User() {
    }
}
