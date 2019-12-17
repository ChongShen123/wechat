package com.cxkj.wechat.constant;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.entity.FriendApplication;

/**
 * @author tiankong
 * @date 2019/12/12 18:26
 */
public interface SystemConstant {
    // -------------------------------好友申请相关常量---------------------------------------------------------
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

    String RETURN_MESSAGE = "我们已经是好友啦！";
    // ---------------------------------参数获取相关常量-----------------------------------------------

    /**
     * 命令
     */
    String KEY_COMMAND = "command";
    /**
     * ID
     */
    String KEY_ID = "id";
    /**
     * 用户ids
     */
    String KEY_IDS = "ids";

    /**
     * 状态
     */
    String KEY_STATE = "state";
    /**
     * 接收者
     */
    String KEY_TO_USER_ID = "toUserId";


    /**
     * 内容
     */
    String KEY_CONTENT = "content";

    /**
     * 状态
     */
    String KEY_TYPE = "type";

    /**
     * 用户名
     */
    String KEY_USERNAME = "username";

    /**
     * 群ID
     */
    String KEY_GROUP_ID = "group_id";

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

    // --------------------------RabbitMQ 相关常量-----------------------------------------------
    /**
     * 交换器名字
     */
    String DIRECT_NAME = "chat-direct";
    /**
     * RabbitMQ直连模式
     */
    String DIRECT_PATTERN = "direct";
    /**
     * 单聊队列1
     */
    String SINGLE_CHAT_QUEUE_ONE = "single-chat-queue-1";
    /**
     * 单聊队列2
     */
    String SINGLE_CHAT_QUEUE_TOW = "single-chat-queue-2";
    /**
     * 群聊队列1
     */
    String GROUP_CHAT_QUEUE_ONE = "group-chat-queue-1";
    /**
     * 群聊队列2
     */
    String GROUP_CHAT_QUEUE_TOW = "group-chat-queue-2";

    // -------------------文件操作------------------------------------------------
    /**
     * 上传文件的大小
     */
    long UPLOAD_FILE_SIZE = 1024 * 1024;
    /**
     * 上传文件支持的格式
     */
    String[] SUFFIX_ARRAY = {".png", ".jpg"};


    // -----------------------系统通知相关----------------------------------------
    /**
     * 系统通知ID
     */
    Integer SYSTEM_USER_ID = 0;
}
