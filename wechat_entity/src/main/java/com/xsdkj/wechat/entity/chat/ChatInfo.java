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
    @Id
    protected String id;
    protected Integer fromUserId;
    protected String content;
    /**
     * 0信息 1语音 2图片 3撤销 4 加群 5退群 6红包 7转账
     */
    protected Byte type;
    protected Long createTimes;
}
