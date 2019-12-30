package com.xsdkj.wechat.constant;

/**
 * @author tiankong
 * @date 2019/12/30 12:19
 */
public interface ChatConstant {
    // -------------------------------好友相关常量---------------------------------------------------------
    /**
     * 未处理
     */
    byte UNTREATED = 0;
    /**
     * 同意
     */
    byte AGREE = 1;
    /**
     * 拒绝
     */
    byte REFUSE = 2;
    /**
     * 添加好友
     */
    byte ADD_FRIEND = 0;
    /**
     * 回复好友
     */
    byte RETURN_FRIEND = 1;

    String RETURN_MESSAGE_SUCCESS = "我们已经是好友啦！";
    String RETURN_MESSAGE_REFUSE = "拒绝添加你为好友!";
    // ------------------消息回复相关常量----------------------------------------------------------
    /**
     * 成功
     */
    String MSG_SUCCESS = "发送成功";
    // ------------------------聊天消息类型--------------------------------------------
    /**
     * 字符串
     */
    byte CHAT_TYPE_MSG = 0;
    /**
     * 语音
     */
    byte CHAT_TYPE_VOICE = 1;
    /**
     * 图片
     */
    byte CHAT_TYPE_IMG = 2;
    /**
     * 撤销
     */
    byte CHAT_TYPE_REVOCATION = 3;
    /**
     * 加入群聊
     */
    byte JOIN_GROUP = 4;

    /**
     * 退群
     */
    byte QUIT_GROUP = 5;
}
