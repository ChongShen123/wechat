package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/15 12:47
 */
@Data
public class GroupVo implements Serializable {
    private static final long serialVersionUID = 1360634805266076582L;
    private Integer gid;
    private String groupName;
    private String icon;
    /**
     * 是否屏蔽消息
     */
    private Boolean noticeType;
}
