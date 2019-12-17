package com.cxkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserUpdateInfoParam {
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "头像不能为空")
    private  String icon;

    @NotNull(message = "手机不能为空")
    private  String tel;

    @NotNull(message = "QQ不能为空")
    private  String qq;
    @Email
    private  String email;




}
