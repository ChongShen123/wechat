package com.xsdkj.wechat.constant;

/**
 * @author tiankong
 * @date 2019/12/30 11:59
 */
public interface RedisConstant {
    /**
     * redis群组禁言key
     */
    String REDIS_GROUP_NO_SAY = "group_no_say";
    /**
     * 缓存群信息
     * group_+群id
     */
    String REDIS_GROUP_KEY = "group_";
    /**
     * 群所有用户
     * group_members_+群id
     */
    String REDIS_GROUP_MEMBERS = "group_members_";
    /**
     * 用户key
     * user_+username
     */
    String REDIS_USER_ID = "user_";

    /**
     * 系统参数key
     */
    String REDIS_SYSTEM_PARAMETER = "system_parameter";

    /**
     * 用户缓存过期时间
     * 单位s
     */
    long REDIS_USER_TIMEOUT = 60 * 60 * 24;
}
