package com.cxkj.wechat.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/14 13:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SingleChatCancelVo extends ChatResponse {
    private Long createTimes;
    private String id;

    public SingleChatCancelVo(String id) {
        this.id = id;
        createTimes = System.currentTimeMillis();
    }
}
