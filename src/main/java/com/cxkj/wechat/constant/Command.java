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
    int HEARTBEAT = 1000;
    /**
     * 注册用户channel
     */
    int REGISTER = 1001;
    /**
     * 单聊
     */
    int SINGLE_CHAT = 1002;
    /**
     * 撤销单聊消息
     */
    int SINGLE_CHAT_CANCEL = 1003;
    // -------------------------好友相关-----------------------------------
    /**
     * 添加好友
     */
    int ADD_FRIEND = 2000;

    /**
     * 好友回复
     */
    int FRIEND_AGREE = 2001;
    /**
     * 查询好友申请消息
     */
    int LIST_FRIEND_APPLICATION = 2002;
    /**
     * 删除好友
     */
    int DELETE_FRIEND = 2003;

    //  ---------------------------群聊相关--------------------------------
    /**
     * 创建群聊
     */
    int CREATE_GROUP = 3000;
    /**
     * 查看用户所有群
     */
    int LIST_GROUP = 3001;
    /**
     * 群基本信息
     */
    int GROUP_BASE_INFO = 3002;

    /**
     * 群详情
     */
    int GROUP_INFO = 3003;
    /**
     * 群聊
     */
    int GROUP_CHAT = 3001;
    /**
     * 退出群聊
     */
    int QUIET_GROUP = 3003;

    /**
     * 加入群聊
     */
    int JOIN_GROUP = 3004;

    /**
     * 加入群聊消息通知
     */
    int JOIN_GROUP_NOTICE = 3005;

    /**
     * 查看群成员
     */
    int LIST_GROUP_MEMBERS = 3006;


}
