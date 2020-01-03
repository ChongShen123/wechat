package com.xsdkj.wechat.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 请求参数
 *
 * @author tiankong
 * @date 2019/12/15 15:09
 */
@Data
public class RequestParamBo {
    private BigDecimal price;
    private Integer groupId;
    private Integer userId;
    private Integer toUserId;
    private Integer friendId;
    private Integer intType;
    private Long times;
    private String name;
    private String id;
    private String token;
    private String username;
    private String content;
    private String message;
    private String icon;
    private String qr;
    private String notice;
    private Byte byteType;
    private Byte state;
    private Boolean addFriend;
    private Set<Integer> ids;
}
