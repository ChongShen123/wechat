package com.xsdkj.wechat.entity.chat;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 好友申请消息
 *
 * @author tiankong
 * @date 2019/12/12 13:50
 */
@Data
public class FriendApplication implements Serializable {
    private static final long serialVersionUID = 3299136926378775672L;
    @Id
    private String id;
    private Integer toUserId;
    private Integer fromUserId;
    private String fromUsername;
    private String fromUserIcon;
    private String message;
    /**
     * 0 未处理 1 同意 2 拒绝  注:回复好友消息则这条字段无意义
     */
    private Byte state;
    /**
     * 是否阅读
     */
    private boolean isRead;
    /**
     * 消息类型 0 添加好友 1 回复好友
     */
    private Byte type;
    /**
     * 创建时间
     */
    private Long createTimes;
    /**
     * 创建时间
     */
    private Long modifiedTime;
}
