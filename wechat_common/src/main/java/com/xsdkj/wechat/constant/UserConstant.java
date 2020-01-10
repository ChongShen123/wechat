package com.xsdkj.wechat.constant;

/**
 * 用户相关常量
 *
 * @author tiankong
 * @date 2019/12/30 14:59
 */
public interface UserConstant {
    /**
     * 管理员类型
     */
    Byte TYPE_ADMIN = 1;
    /**
     * 用户类型
     */
    Byte TYPE_USER = 0;


    // 好友申请
    /**
     * 未处理
     */
    Byte UNTREATED = 0;
    /**
     * 同意
     */
    Byte AGREE = 1;
    /**
     * 拒绝
     */
    Byte REFUSE = 2;

}
