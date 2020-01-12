package com.xsdkj.wechat.dto;

import lombok.Data;
@Data
public class UserGroupDto {
    private Integer id;
    private Integer uid;
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
    public Integer ownerId;
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
     * 群类型
     */
    private Byte type;
    /**
     * 群发言类型 0禁止发言 1 允许发言
     */
    private Byte noSayType;
    private Long createTimes;
    private Long modifiedTimes;
    private Integer pageNum;
    private Integer pageSize;

    public UserGroupDto() {
    }
}
