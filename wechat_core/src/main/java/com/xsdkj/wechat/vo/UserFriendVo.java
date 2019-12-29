package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/25 17:17
 */
@Data
public class UserFriendVo implements Serializable {
    private Integer uid;
    private Long uno;
    private String icon;
    private String username;
    private String remarkName;
}
