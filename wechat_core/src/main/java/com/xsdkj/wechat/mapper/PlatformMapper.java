package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.chat.Platform;

/**
 * @author tiankong
 * @date 2020/1/2 17:50
 */
public interface PlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Platform record);

    int insertSelective(Platform record);

    Platform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Platform record);

    int updateByPrimaryKey(Platform record);
}