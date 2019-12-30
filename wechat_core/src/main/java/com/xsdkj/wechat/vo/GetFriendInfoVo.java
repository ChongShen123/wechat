package com.xsdkj.wechat.vo;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/30 19:08
 */
@Data
public class GetFriendInfoVo {
    private Integer id;
    private Long uno;
    private String nickname;
    private Boolean loginState;
    private String icon;
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
     * 上次登录时间
     */
    private Long lastLoginTimes;

}
