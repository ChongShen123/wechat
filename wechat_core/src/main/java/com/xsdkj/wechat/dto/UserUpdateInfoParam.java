package com.xsdkj.wechat.dto;

import lombok.Data;

@Data
public class UserUpdateInfoParam {
    //TODO 将所有是ID 的修改为Uno
    private Integer id;
    private String icon;
    private String tel;
    private String qq;
    private String email;
}
