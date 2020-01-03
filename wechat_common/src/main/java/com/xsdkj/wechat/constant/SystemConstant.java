package com.xsdkj.wechat.constant;

/**
 * 系统常量
 *
 * @author tiankong
 * @date 2019/12/12 18:26
 */
public interface SystemConstant {
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
     * 机器人接口
     * 要付费的..(⊙﹏⊙)b
     */
//    String ROBOT_URL = "https://api.ownthink.com/bot?appid=xiaosi&userid=user&spoken=";
    String ROBOT_URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";
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
     * 20s
     */
    Long CHAT_CANCEL_TIMES = 2000 * 10L;

    /**
     * 开启
     */
    Integer OPEN = 1;

    /**
     * 关闭
     */
    Integer OFF = 0;
    /**
     * 充值
     */
    int TOP_UP = 1;

    /**
     * 提现
     */
    int DRAW_MONEY = 2;
}
