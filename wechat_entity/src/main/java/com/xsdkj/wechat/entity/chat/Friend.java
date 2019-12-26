package com.xsdkj.wechat.entity.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/12 18:53
 */
@Data
public class Friend implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer fid;

    /**
     * 标签(对联系人进行分类)
     */
    private String flag;

    /**
     * 备注名
     */
    private String remarkName;

    private String tel;

    /**
     * 描述
     */
    private String description;

    /**
     * 名片或相关图片
     */
    private String img;

    /**
     * 状态 0 正常 1黑名单 2 标星朋友 3屏蔽
     */
    private Byte state;

    private Long createTimes;

    private Long modifiedTimes;

    private static final long serialVersionUID = 1L;
}
