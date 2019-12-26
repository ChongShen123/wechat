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
public class RabbitMessageBox implements Serializable {
    private Integer type;
    private Object data;

    public RabbitMessageBox() {
    }

    private RabbitMessageBox(Integer type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static RabbitMessageBox createBox(Integer type, Object data) {
        return new RabbitMessageBox(type, data);
    }
}
