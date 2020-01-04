package com.xsdkj.wechat.constant;

/**
 * @author tiankong
 * @date 2020/1/4 14:22
 */
public interface WalletConstant {
    // ----------------用户账变类型--------------
    /**
     * 充值
     */
    byte TOP_UP = 1;
    /**
     * 提现
     */
    byte WITHDRAW = 2;
    /**
     * 转账
     */
    byte TRANSFER = 3;
    /**
     * 红包
     */
    byte RED_PACKAGE = 4;

    // ---------------转账类型--------------------
    /**
     * 转入
     */
    byte SHIFT_IN = 1;

    /**
     * 转出
     */
    byte SHIFT_OUT = 0;
}
