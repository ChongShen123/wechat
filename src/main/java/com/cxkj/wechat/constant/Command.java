package com.cxkj.wechat.constant;

/**
 * @author tiankong
 * @date 2019/11/18 9:58
 */
public interface Command {
    // --------------------------基本------------------------------
    /**
     * 心跳
     */
    byte HEARTBEAT = 0;
    /**
     * 注册用户channel
     */
    byte REGISTER = 1;
    /**
     * 单聊
     */
    byte SINGLE_CHAT = 2;
    /**
     * 撤销单聊消息
     */
    byte SINGLE_CHAT_CANCEL = 19;
    //  ---------------------------群聊相关--------------------------------
    /**
     * 退出群聊
     */
    byte QUIET_GROUP = 8;

    /**
     * 加入群聊
     */
    byte JOIN_GROUP = 7;

    /**
     * 加入群聊消息通知
     */
    byte JOIN_GROUP_NOTICE = 71;

    /**
     * 查看所有群成员
     */
    byte LIST_GROUP_ALL_MEMBERS = 6;

    /**
     * 查看群成员
     */
    byte LIST_GROUP_MEMBERS = 5;

    /**
     * 创建群聊
     */
    byte CREATE_GROUP = 4;

    /**
     * 群聊
     */
    byte GROUP_CHAT = 3;

    // -------------------------好友相关-----------------------------------
    /**
     * 添加好友
     */
    byte ADD_FRIEND = 15;

    /**
     * 好友回复
     */
    byte FRIEND_AGREE = 16;
    /**
     * 查询好友申请消息
     */
    byte LIST_FRIEND_APPLICATION = 17;

}
