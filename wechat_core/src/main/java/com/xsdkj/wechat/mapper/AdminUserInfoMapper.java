package com.xsdkj.wechat.mapper;


import com.xsdkj.wechat.dto.UserGroupDto;
import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.dto.UserMoodDto;
import com.xsdkj.wechat.dto.UserWalletDto;
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

    User selectUserInfo(Integer id);

    List<UserLoginLogoDto> selectUserLoginLog(Integer id);

    List<UserWalletDto> selectUserWallet(UserWalletDto UserWalletDto);

    List<UserMoodDto> selectUserMood(UserMoodDto UserMoodDto);

    List<UserGroupDto> selectUserGroup(UserGroupDto userGroupDto);
}
