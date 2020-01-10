package com.xsdkj.wechat.entity.chat;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/17 13:17
 */
@Data
public class ChatInfo implements Serializable {
    private static final long serialVersionUID = 20376938522084436L;
    @Id
    protected String id;
    protected Integer fromUserId;
    protected String content;
    /**
     * 0信息 1语音 2图片 3撤销 4 加群 5退群 6红包 7转账 8 发朋友圈 9 评论 10 点赞
     */
    protected Byte type;
    protected Long createTimes;
}
