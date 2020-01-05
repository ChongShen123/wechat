package com.xsdkj.wechat.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.xsdkj.wechat.entity.wallet.SignAward;

/**
 * @author tiankong
 * @date 2020/1/5 11:22
 */
public interface SignAwardMapper {
    int deleteByPrimaryKey(Short id);

    int insert(SignAward record);

    int insertSelective(SignAward record);

    SignAward selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(SignAward record);

    int updateByPrimaryKey(SignAward record);

    List<SignAward> listAll();

}
