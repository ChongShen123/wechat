package com.xsdkj.wechat.dto;

import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/13 12:26
 */
@Data
public class ListUserScoreDto {
    private Integer uid;
    private String username;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
