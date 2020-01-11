package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tiankong
 * @date 2020/1/11 14:54
 */
@Data
public class UserPriceOperationLogDto {
    private Integer uid;
    private String username;
    private String uno;
    private Long beginTimes;
    private Long endTimes;
    private Integer dateType;
    private Integer operationType;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
