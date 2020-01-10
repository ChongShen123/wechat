package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;

/**
 * 禁止聊天异常
 *
 * @author tiankong
 * @date 2019/12/30 12:01
 */
public class BannedChatException extends ServiceException {
    private static final long serialVersionUID = 190346863493186408L;

    public BannedChatException() {
        code = ResultCodeEnum.BANNED_CHAT;
    }
}
