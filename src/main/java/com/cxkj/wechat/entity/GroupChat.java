package com.cxkj.wechat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 群聊消息
 *
 * @author tiankong
 * @date 2019/12/17 13:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collation = "GroupChat")
public class GroupChat extends ChatInfo implements Serializable {
    private Integer toGroupId;

    @Override
    public String toString() {
        return "GroupChat{" +
                "toGroupId=" + toGroupId +
                ", id='" + id + '\'' +
                ", fromUserId=" + fromUserId +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", createTimes=" + createTimes +
                '}';
    }
}
