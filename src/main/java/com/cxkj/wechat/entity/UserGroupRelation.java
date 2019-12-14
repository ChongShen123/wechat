package com.cxkj.wechat.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
@Data
public class UserGroupRelation implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer gid;

    /**
    * 群昵称
    */
    private String groupNickname;

    /**
    * 是否屏蔽消息
    */
    private Boolean noticeType;

    private static final long serialVersionUID = 1L;
}