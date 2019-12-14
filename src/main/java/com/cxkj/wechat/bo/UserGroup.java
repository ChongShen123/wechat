package com.cxkj.wechat.bo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/12 11:09
 */
@Data
public class UserGroup implements Serializable {
    private String groupName;
    private Integer gid;
    private String icon;

    public UserGroup() {
    }
}
