package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.other.NicknameName;import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/2 17:51
 */
public interface NicknameNameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NicknameName record);

    int insertSelective(NicknameName record);

    NicknameName selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NicknameName record);

    int updateByPrimaryKey(NicknameName record);

    List<NicknameName> getByAll(NicknameName nicknameName);
}
