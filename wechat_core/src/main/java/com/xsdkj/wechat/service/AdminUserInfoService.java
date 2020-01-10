package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.entity.user.User;

import java.util.List;

public interface AdminUserInfoService {
    Integer selectRegisterCount(Integer platformId);

    Integer selectLastLoginTimes(Integer platformId);

    Integer selectOnlineUser(Integer platformId);

    List<User> selectUserInfo(Integer id);

    List<UserLoginLogoDto> selectUserLoginLog(Integer id);
}
