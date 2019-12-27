package com.xsdkj.wechat.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息队列信息箱
 *
 * @author tiankong
 * @date 2019/12/26 13:44
 */
@Data
public class RabbitMessageBoxBo implements Serializable {
    private Integer type;
    private Object data;

    public RabbitMessageBoxBo() {
    }

    private RabbitMessageBoxBo(Integer type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static RabbitMessageBoxBo createBox(Integer type, Object data) {
        return new RabbitMessageBoxBo(type, data);
    }
}
