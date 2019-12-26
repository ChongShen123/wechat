package com.xsdkj.wechat.constant;

import com.xsdkj.wechat.bo.SessionBo;
import io.netty.util.AttributeKey;

/**
 * @author tiankong
 * @date 2019/11/17 19:36
 */
public interface Attributes {
    AttributeKey<SessionBo> SESSION = AttributeKey.newInstance("session");
    AttributeKey<Boolean> IS_LOGIN = AttributeKey.newInstance("isLogin");
}
