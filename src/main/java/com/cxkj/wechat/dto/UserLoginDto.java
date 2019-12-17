package com.cxkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2019/12/11 18:37
 */
@Data
public class UserLoginDto {
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
