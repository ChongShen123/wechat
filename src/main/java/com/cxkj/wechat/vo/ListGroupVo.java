package com.cxkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/15 12:47
 */
@Data
public class ListGroupVo implements Serializable {
    private Integer gid;
    private String groupName;
    private String icon;
    /**
     * 是否屏蔽消息
     */
    private Boolean noticeType;
}
