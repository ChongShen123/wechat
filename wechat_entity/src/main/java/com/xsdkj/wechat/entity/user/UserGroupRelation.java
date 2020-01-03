package com.xsdkj.wechat.entity.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/28 16:09
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
