package com.xsdkj.wechat.service;

import com.xsdkj.wechat.vo.UserScoreVo;

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
}
