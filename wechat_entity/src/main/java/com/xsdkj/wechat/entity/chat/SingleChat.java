package com.xsdkj.wechat.entity.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/13 11:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SingleChat extends ChatInfo implements Serializable {
    private Integer toUserId;
    private Boolean read;

    @Override
    public String toString() {
        return "SingleChat{" +
                "toUserId=" + toUserId +
                ", read=" + read +
                ", id='" + id + '\'' +
                ", fromUserId=" + fromUserId +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", createTimes=" + createTimes +
                '}';
    }
}
