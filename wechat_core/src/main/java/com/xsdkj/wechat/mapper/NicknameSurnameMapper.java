package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.chat.NicknameSurname;import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/2 17:51
 */
public interface NicknameSurnameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NicknameSurname record);

    int insertSelective(NicknameSurname record);

    NicknameSurname selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NicknameSurname record);

    int updateByPrimaryKey(NicknameSurname record);

    List<NicknameSurname> getByAll(NicknameSurname nicknameSurname);
}