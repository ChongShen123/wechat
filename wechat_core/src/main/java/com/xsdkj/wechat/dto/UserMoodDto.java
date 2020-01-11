package com.xsdkj.wechat.dto;

import lombok.Data;

@Data
public class UserMoodDto {
    private Integer id;
    private Integer uid;
    private String content;
    private String file;
    private Byte fileType;
    private Integer commentCount;
    private Long createTimes;
    private Integer pageNum;
    private Integer pageSize;

}
