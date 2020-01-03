package com.xsdkj.wechat.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2019/12/11 13:32
 */
@Data
public class UserRegisterDto {
    @NotNull(message = "平台id不能为空")
    private Integer platformId;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "邮箱不能为空")
    @Email
    private String email;
}
