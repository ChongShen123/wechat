package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/30 14:48
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = 3452773848565100595L;
    private Integer id;
    private Long uno;
    private String username;
    private String nickname;
    private String icon;
    private Byte gender;
    private String qr;
    private String email;
    private String qq;
    private String description;
    private Byte type;
    private Long lastLoginTimes;
}
