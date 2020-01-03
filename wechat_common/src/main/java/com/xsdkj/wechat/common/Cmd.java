package com.xsdkj.wechat.common;

/**
 * @author tiankong
 * @date 2019/11/18 9:58
 */
public interface Cmd {
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

    /**
     * 机器人聊天
     */
    int ROBOT_CHAT = 1004;
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
     * 查看好友列表
     */
    int LIST_FRIEND = 2003;

    /**
     * 删除好友
     */
    int DELETE_FRIEND = 2004;
    /**
     * 查看好友信息
     */
    int GET_FRIEND_INFO = 2005;
    //  ---------------------------群聊相关--------------------------------
    /**
     * 创建群聊
     */
    int CREATE_GROUP = 3000;
    /**
     * 查看用户所有群
     */
    int LIST_USER_GROUP = 3001;
    /**
     * 群基本信息
     */
    int GROUP_BASE_INFO = 3002;

    /**
     * 群详情
     */
    int GROUP_INFO = 3003;
    /**
     * 加入群聊
     */
    int JOIN_GROUP = 3004;

    /**
     * 查看群所有成员
     */
    int LIST_GROUP_MEMBERS = 3005;

    /**
     * 群聊
     */
    int GROUP_CHAT = 3006;

    /**
     * 移除群聊
     * (群管理员操作)
     */
    int REMOVE_CHAT_GROUP = 3007;

    /**
     * 退出群聊
     */
    int QUIT_GROUP = 3008;

    /**
     * 解散群组
     */
    int DISSOLVE_GROUP = 3009;

    /**
     * 禁言
     */
    int NO_SAY = 3010;

    /**
     * 设置群管理员
     */
    int SET_GROUP_MANAGER = 3011;

    /**
     * 设置群是否可聊天
     */
    int SET_GROUP_CHAT = 3012;

    /**
     * 修改群信息
     */
    int UPDATE_GROUP_INFO = 3013;

    //----------------------------------- 用户相关 -----------------------------------
    /**
     * 查询用户详情
     */
    int USER_INFO = 4000;
}
