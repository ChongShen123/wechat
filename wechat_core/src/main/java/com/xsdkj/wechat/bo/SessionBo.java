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
    private Byte type;
    /**
     * 平台id
     */
    private Integer platformId;

    public SessionBo(Integer userId, String username, String icon, Integer platformId, Byte type) {
        this.uid = userId;
        this.username = username;
        this.icon = icon;
        this.platformId = platformId;
        this.type = type;
    }
}
