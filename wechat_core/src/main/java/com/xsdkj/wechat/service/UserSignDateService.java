package com.xsdkj.wechat.service;


import com.xsdkj.wechat.dto.*;
import com.xsdkj.wechat.vo.UserSignDateDetailVo;
import com.xsdkj.wechat.vo.UserSignDateVo;

import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/5 11:25
 */
public interface UserSignDateService {
    /**
     * 用户签到
     */
    void handleSignDate();

    /**
     * 送补签次数
     *
     * @param giveRetroactiveCountDto 参数
     */
    void handleGiveRetroactiveCount(GiveRetroactiveCountDto giveRetroactiveCountDto);


    /**
     * 用户补签
     *
     * @param retroactiveDto 参数
     */
    void handleRetroactive(RetroactiveDto retroactiveDto);

    /**
     * 查询用户签到情况
     *
     * @param userSignDateDto 参数
     * @return list
     */
    List<UserSignDateVo> listSignDate(UserSignDateDto userSignDateDto);

    /**
     * 查询用户签到详情
     *
     * @param param 用户id
     * @return List
     */
    List<UserSignDateDetailVo> listUserSignDateDetail(UserSignDateDetailDto param);

    /**
     * 赠送用户补签次数 全体
     *
     * @param giveRetroactiveCountDto 参数
     */
    void handleGiveRetroactiveCountAll(Integer giveRetroactiveCountDto);


}
