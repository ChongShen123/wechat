package com.cxkj.wechat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/13 11:30
 */
@Data
public class SingleChat implements Serializable {
    @Id
    private String id;
    private Integer toUserId;
    private Integer fromUserId;
    private String content;
    private Boolean read;
    /**
     * 0信息 1语音 2图片 3撤销 4 加入群聊 5红包 6转账
     */
    private Byte type;
    private Long createTimes;

    @Override
    public String toString() {
        return "SingleChat{" +
                "id='" + id + '\'' +
                ", toUserId=" + toUserId +
                ", fromUserId=" + fromUserId +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", createTimes=" + createTimes +
                ", read=" + read +
                '}';
    }
}
