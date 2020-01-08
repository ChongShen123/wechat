package com.xsdkj.wechat.service;


import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.dto.GiveScoreDto;

/**
 * @author tiankong
 * @date 2020/1/5 11:25
 */
public interface UserSignDateService {
    /**
     * 用户签到
     */
    void singDate();

    /**
     * 送补签次数
     *
     * @param giveRetroactiveCountDto 参数
     */
    void giveRetroactiveCount(GiveRetroactiveCountDto giveRetroactiveCountDto);

    /**
     * 赠送用户积分
     *
     * @param giveScoreDto 参数
     */
    void giveScore(GiveScoreDto giveScoreDto);
}
