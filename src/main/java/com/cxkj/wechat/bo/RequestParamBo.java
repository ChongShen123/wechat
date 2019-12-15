package com.cxkj.wechat.bo;

import lombok.Data;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/15 15:09
 */
@Data
public class RequestParamBo {
    private String id;
    private String token;
    private String username;
    private Integer userId;
    private Integer toUserId;
    private String content;
    private String message;
    private Integer type;
    private Byte state;
    private Integer groupId;
    private List<Integer> ids;
}
