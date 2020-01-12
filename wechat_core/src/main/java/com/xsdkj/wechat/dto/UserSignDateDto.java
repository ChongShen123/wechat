package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2020/1/6 14:52
 */
@Data
public class UserSignDateDto {
    private Integer year;
    private Integer month;
    private Integer uid;
    private String username;
    private Integer pageNum = 1;
    private Integer pageSize = 4;
    private Integer platformId;
}
