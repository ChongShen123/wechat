package com.cxkj.wechat.dto;

import lombok.Data;

@Data
public class UserUpdatePassword {
    Integer id;
    String passWord;
    String newPassWord;

}
