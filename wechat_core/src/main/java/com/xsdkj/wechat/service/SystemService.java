package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UpdateSignDateDto;
import com.xsdkj.wechat.entity.wallet.SystemParameter;
import com.xsdkj.wechat.vo.SystemSignDateVo;

/**
 * @author tiankong
 * @date 2020/1/5 19:58
 */
public interface SystemService {
    /**
     * 获取最大补签次数限制
     *
     * @param type 是否在缓存获取
     * @return SystemParameter
     */
    SystemParameter getSystemParameter(boolean type);

    /**
     * 查询签到相关系统参数
     *
     * @return SystemSignDateVo
     */
    SystemSignDateVo getSinDate();

    /**
     * 修改签到相关的系统参数
     * @param updateSignDateDto 参数
     * @return SystemParameter
     */
    SystemParameter updateSignDate(UpdateSignDateDto updateSignDateDto);

    /**
     * 更新redis缓存
     */
    void updateRedisSignDate();
}
