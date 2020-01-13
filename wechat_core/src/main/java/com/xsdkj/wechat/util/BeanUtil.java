package com.xsdkj.wechat.util;

import com.xsdkj.wechat.entity.wallet.UserScore;

/**
 * @author tiankong
 * @date 2020/1/13 11:31
 */
public class BeanUtil {
    public static UserScore createNewUserScore(Integer userId) {
        UserScore userScore = new UserScore();
        userScore.setUid(userId);
        userScore.setRetroactiveCount(0);
        userScore.setSuccessionCount(0);
        userScore.setScore(0);
        userScore.setLastSignDateTimes(System.currentTimeMillis());
        return userScore;
    }
}
