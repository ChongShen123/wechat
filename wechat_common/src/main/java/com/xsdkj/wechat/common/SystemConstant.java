package com.xsdkj.wechat.common;

/**
 * 系统常量
 *
 * @author tiankong
 * @date 2019/12/12 18:26
 */
public interface SystemConstant {
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
    // ---------------------------------参数获取相关常量-----------------------------------------------

    /**
     * 命令
     */
    String KEY_CMD = "cmd";
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
     * token
     */
    String KEY_TOKEN = "token";

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
    /**
     * 好友ID
     */
    String KEY_FRIEND_ID = "friend_id";

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

    // --------------------------RabbitMQ 相关常量-----------------------------------------------
    /**
     * 直连模式交换器名字
     */
    String DIRECT_NAME = "chat-direct";
    /**
     * fanout 分列模式聊天交换器名字
     */
    String FANOUT_CHAT_NAME = "chat-fanout";
    /**
     * 服务交换器
     */
    String FANOUT_SERVICE_NAME = "service-fanout";
    /**
     * 直连模式
     */
    String DIRECT_PATTERN = "direct";
    /**
     * 聊天主队列
     */
    String CHAT_QUEUE_MAIN = "chat-queue-main";
    /**
     * 聊天辅队列
     */
    String CHAT_QUEUE_ASSIST = "chat-queue-assist";
    /**
     * 服务主队列
     */
    String SERVICE_QUEUE_MAIN = "service-queue-main";
    /**
     * 服务辅队列
     */
    String SERVICE_QUEUE_ASSIST = "service-queue-assist";

    /**
     * 群聊队列1
     */
    String GROUP_CHAT_QUEUE_MAIN = "group-chat-main";
    /**
     * 群聊队列2
     */
    String GROUP_CHAT_QUEUE_ASSIST = "group-chat-assist";

    /**
     * 单聊类型
     */
    int BOX_TYPE_SING_CHAT = 1;
    /**
     * 群聊类型
     */
    int BOX_TYPE_GROUP_CHAT = 2;
    // -------------------文件操作------------------------------------------------
    /**
     * 上传文件的大小
     */
    long UPLOAD_FILE_SIZE = 1024 * 1024;
    /**
     * 上传文件支持的格式
     */
    String[] SUFFIX_ARRAY = {".png", ".jpg"};


    // -----------------------系统相关----------------------------------------
    /**
     * 系统账号生成最小值
     */
    int UNO_MIN = 1000000;
    /**
     * 系统账号生成最大值
     */
    int UNO_MAX = 3000000;
    /**
     * 邮箱正则
     */
    String EMAIL_REGEX = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 用户密码正则
     */
    String PASSWORD_REGEX = "^[0-9A-Za-z_]{6,12}$";
    /**
     * 系统通知ID
     */
    Integer SYSTEM_USER_ID = 0;

    /**
     * 系统消息撤销最大时间
     * 10s
     */
    Long CHAT_CANCEL_TIMES = 1000 * 10L;

    // ----------------redis 相关-----------------------------------------------------
    /**
     * 用户key
     * user_+username
     */
    String REDIS_USER_KEY = "user_";

    /**
     * 用户好友列表
     * user_friend_+用户id
     */
    String REDIS_USER_FRIEND = "user_friend_";

    /**
     * 用户缓存过期时间
     * 单位s
     */
    long REDIS_USER_TIMEOUT = 60 * 60 * 24;


}
