package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.chat.Platform;

/**
 * @author tiankong
 * @date 2019/12/31 15:50
 */
public interface PlatformService {
    /**
     * 根据id查询
     * @param platformId id
     * @return Platform
     */
    Platform getById(Integer platformId);
}
