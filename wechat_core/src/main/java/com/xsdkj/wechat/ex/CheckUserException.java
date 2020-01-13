package com.xsdkj.wechat.ex;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/12 20:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CheckUserException extends ServiceException {
    private static final long serialVersionUID = -5032878627572720672L;
    private String msg;

    public CheckUserException(String string) {
        this.msg = string;
    }
}
