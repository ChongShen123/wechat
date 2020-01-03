package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.platform.Platform;
import com.xsdkj.wechat.mapper.PlatformMapper;
import com.xsdkj.wechat.service.PlatformService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/31 15:53
 */
@Service
public class PlatformServiceImpl implements PlatformService {
    @Resource
    private PlatformMapper platformMapper;

    @Override
    public Platform getById(Integer platformId) {
        return platformMapper.selectByPrimaryKey(platformId);
    }
}
