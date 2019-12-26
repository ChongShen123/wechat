package com.xsdkj.wechat.vo;

import com.xsdkj.wechat.entity.chat.FriendApplication;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/13 11:01
 */
@Data
public class FriendApplicationVo {
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
