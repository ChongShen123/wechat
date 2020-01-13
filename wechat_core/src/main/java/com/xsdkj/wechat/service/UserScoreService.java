package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.dto.ListUserScoreDto;
import com.xsdkj.wechat.entity.wallet.UserScore;
import com.xsdkj.wechat.vo.UserScoreVo;

import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/6 15:32
 */
public interface UserScoreService {

    /**
     * 查询用户积分
     *
     * @return UserScoreVo
     */
    UserScoreVo getUserScore();

    /**
     * 保存
     *
     * @param newUserScore 参数
     */
    void save(UserScore newUserScore);

    /**
     * 查询用户积分
     *
     * @param listUserScoreDto 参数
     * @return list
     */
    List<UserScoreVo> listUserScore(ListUserScoreDto listUserScoreDto);

    /**
     * 赠扣积分
     *
     * @param giveScoreDto 参数
     */
    void handleGiveScore(GiveScoreDto giveScoreDto);

    /**
     * 赠送全体
     *
     * @param score 积分
     */
    void handleGiveScoreAll(Integer score);
}
