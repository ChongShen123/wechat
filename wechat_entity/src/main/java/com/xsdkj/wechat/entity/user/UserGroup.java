package com.xsdkj.wechat.entity.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/30 11:55
 */
@Data
public class UserGroup implements Serializable {
    private Integer id;

    private String name;

    /**
     * 群头像
     */
    private String icon;

    /**
     * 群二维码
     */
    private String qr;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 群主id
     */
    private Integer ownerId;

    /**
     * 群成员个数
     */
    private Integer membersCount;

    /**
     * 是否禁言
     */
    private Boolean state;

    /**
     * 是否开启添加好友 0关闭 1开启(关闭则该群成员不能相互加好友
     */
    private Boolean addFriendType;

    /**
     * 是否保存到数据库
     */
    private Boolean isSave;

    /**
     * 群类型
     */
    private Byte type;

    /**
     * 群发言类型 0禁止发言 1 允许发言
     */
    private Byte noSayType;

    private Long createTimes;

    private Long modifiedTimes;

    private static final long serialVersionUID = 1L;
}
