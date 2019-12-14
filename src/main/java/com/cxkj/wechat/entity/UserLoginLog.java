package com.cxkj.wechat.entity;

import java.io.Serializable;

import com.cxkj.wechat.util.IpUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

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
