package com.cxkj.wechat.bo;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/12 10:10
 */
@Data
public class Session {
    private Integer userId;
    private String username;
    private String icon;

    public Session(Integer userId, String username, String icon) {
        this.userId = userId;
        this.username = username;
        this.icon = icon;
    }
}
