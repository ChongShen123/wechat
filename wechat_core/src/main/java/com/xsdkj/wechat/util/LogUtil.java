package com.xsdkj.wechat.util;

import com.xsdkj.wechat.entity.user.UserOperationLog;

/**
 * @author tiankong
 * @date 2020/1/5 16:10
 */
public class LogUtil {
    /**
     * 创建管理员操作记录
     *
     * @param uid        用户id
     * @param platformId 平台id
     * @param type       操作类型 1 上分,2 下分,3 禁言,4 设置群可加好友 5 加签到次数,6 减签到次数 7 赠送补签次数...
     * @param content    操作内容
     * @return UserOperationLog
     */
    public static UserOperationLog createNewUserOperationLog(Integer uid, Integer platformId, int type, String content) {
        UserOperationLog log = new UserOperationLog();
        log.setUid(uid);
        log.setPlatfromId(platformId);
        log.setOperationType((byte) type);
        log.setContent(content);
        log.setCreateTimes(System.currentTimeMillis());
        return log;
    }
}
