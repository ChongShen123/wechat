package com.cxkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserUpdateInfoParam {
    private Integer id;
    private  String icon;
    private  String tel;
    private  String qq;
    private  String email;
}
