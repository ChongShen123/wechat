package com.xsdkj.wechat.dto;

import lombok.Data;

@Data
public class UserUpdateInfoParam {
    private Integer id;
    private String icon;
    private String tel;
    private String qq;
    private String email;
}
