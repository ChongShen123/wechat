package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UserGroupDto;
import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.dto.UserMoodDto;
import com.xsdkj.wechat.dto.UserWalletDto;
import com.xsdkj.wechat.entity.user.User;

import java.util.List;

public interface AdminUserInfoService {
    Integer selectRegisterCount(Integer platformId);

    Integer selectLastLoginTimes(Integer platformId);

    Integer selectOnlineUser(Integer platformId);

    User selectUserInfo(Integer id);

    List<UserLoginLogoDto> selectUserLoginLog(UserLoginLogoDto userLoginLogoDto);

    List<UserWalletDto> selectUserWallet(UserWalletDto userWalletDto);

    List<UserMoodDto> selectUserMood(UserMoodDto userMoodDto);

    List<UserGroupDto> selectUsergroup(UserGroupDto userGroupDto );
}
