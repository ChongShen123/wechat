package com.xsdkj.wechat.entity.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
@Data
public class UserLoginLog implements Serializable {
    private Integer id;

    private Integer uid;

    private String ip;

    private Long createTimes;

    private static final long serialVersionUID = 1L;

    public UserLoginLog(User user, String ipAddress) {
        uid = user.getId();
        ip = ipAddress;
        createTimes = System.currentTimeMillis();
    }
}
