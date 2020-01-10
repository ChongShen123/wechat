package com.xsdkj.wechat.mapper;


import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.entity.user.User;


import java.util.List;

public interface AdminUserInfoMapper {
    /**
     * 查询平台属性下的注册用户
     * @param platformId
     * @return
     */
    Integer selectRegisterCount(Integer platformId);

    Integer selectLastLoginTimes(Integer platformId);

    Integer selectOnlineUser(Integer platformId);

    List<User> selectUserInfo(Integer id);

    List<UserLoginLogoDto> selectUserLoginLog(Integer id);
}
