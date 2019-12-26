package com.xsdkj.wechat.vo;

import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/14 13:26
 */
@Data
public class SingleChatCancelVo {
    private Long createTimes;
    private String id;

    public SingleChatCancelVo(String id) {
        this.id = id;
        createTimes = System.currentTimeMillis();
    }
}
