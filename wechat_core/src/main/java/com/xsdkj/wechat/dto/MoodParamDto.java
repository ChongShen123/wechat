package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
public class MoodParamDto {
    @NotNull(message = "正文不能为空")
    private String content;
    private String file;
    private Byte fileType;

}
