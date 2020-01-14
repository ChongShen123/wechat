package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UpdateSignAwardDto;
import com.xsdkj.wechat.vo.SignAwardVo;

import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/14 14:57
 */
public interface SignAwardService {
    /**
     * 查询所有签到奖励
     *
     * @return list
     */
    List<SignAwardVo> listAll();

    /**
     * 修改签到奖励
     * @param updateSignAwardDto 参数
     */
    void updateSignAward(UpdateSignAwardDto updateSignAwardDto);
}
