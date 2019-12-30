package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
public class UserCommentDto {
    private  String nickname;
    @NotNull(message = "评论不能为空")
    private String content;
    private String moodId;



}
