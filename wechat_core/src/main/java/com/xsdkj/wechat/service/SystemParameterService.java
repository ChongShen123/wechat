package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.wallet.SystemParameter;

/**
 * @author tiankong
 * @date 2020/1/5 19:58
 */
public interface SystemParameterService {
    /**
     * 获取最大补签次数限制
     *
     * @param type 是否在缓存获取
     * @return SystemParameter
     */
    SystemParameter getSystemParameter(boolean type);
}
