package com.xsdkj.wechat.bo;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/12 10:10
 */
@Data
public class SessionBo {
    private Integer uid;
    private String username;
    private String icon;

    public SessionBo(Integer userId, String username, String icon) {
        this.uid = userId;
        this.username = username;
        this.icon = icon;
    }
}
