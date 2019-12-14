package com.cxkj.wechat.dto;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/11 13:30
 */
@Data
public class LoginInfo {
    private Integer userId;
    private String username;
    private String icon;
    private String token;
    private String qr;

    public LoginInfo(Integer userId, String username, String icon, String token, String qr) {
        this.userId = userId;
        this.username = username;
        this.icon = icon;
        this.token = token;
        this.qr = qr;
    }
}
