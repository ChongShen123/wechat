package com.cxkj.wechat.vo;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/11 13:30
 */
@Data
public class LoginVo {
    private Integer userId;
    private String username;
    private String icon;
    private String token;
    private String qr;

    public LoginVo(Integer userId, String username, String icon, String token, String qr) {
        this.userId = userId;
        this.username = username;
        this.icon = icon;
        this.token = token;
        this.qr = qr;
    }
}
