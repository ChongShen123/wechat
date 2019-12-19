package com.cxkj.wechat.vo;

import com.cxkj.wechat.entity.FriendApplication;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/13 11:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendApplicationVo extends ChatResponse{
    private String id;
    private Integer fromUserId;
    private String fromUsername;
    private String fromUserIcon;
    private String message;
    private Boolean read;
    private Long createTimes;

    public FriendApplicationVo(FriendApplication application) {
        id = application.getId();
        fromUserId = application.getFromUserId();
        fromUsername = application.getFromUsername();
        fromUserIcon = application.getFromUserIcon();
        message = application.getMessage();
        read = application.isRead();
        createTimes = application.getCreateTimes();
    }
}
