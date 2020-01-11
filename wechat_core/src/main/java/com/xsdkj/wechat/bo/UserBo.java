package com.xsdkj.wechat.bo;

import com.xsdkj.wechat.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2020/1/11 10:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBo extends User implements Serializable {
    private static final long serialVersionUID = -7656199410319128384L;
}
