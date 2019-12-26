package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/17 17:49
 */
@Data
public class RemoveChatVo implements Serializable {
    private Integer count;
    private String message;
}
