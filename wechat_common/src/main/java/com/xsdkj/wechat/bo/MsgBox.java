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
public class MsgBox implements Serializable {
    private static final long serialVersionUID = 3354949203382724146L;
    private Integer type;
    private Object data;

    public MsgBox() {
    }

    private MsgBox(Integer type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static MsgBox create(Integer type, Object data) {
        return new MsgBox(type, data);
    }
}
