package com.xsdkj.wechat.dto;

import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/9 13:56
 */
@Data
public class ListUserDto {
    private Integer uid;
    private Integer uno;
    private String username;
    private String tel;
    private Long lastLoginTimes;
    private Boolean loginState;
    private Long createTimes;
    private Integer pageNum;
    private Integer pageSize;
}
