package com.xsdkj.wechat.service.impl;

import com.github.pagehelper.PageHelper;
import com.xsdkj.wechat.dto.UserGroupDto;
import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.dto.UserMoodDto;
import com.xsdkj.wechat.dto.UserWalletDto;
import com.xsdkj.wechat.entity.user.User;

import com.xsdkj.wechat.mapper.AdminUserInfoMapper;
import com.xsdkj.wechat.service.AdminUserInfoService;
import com.xsdkj.wechat.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author panda
 */
@Service
public class AdminUserInfoServiceImpl implements AdminUserInfoService {
    @Resource
    AdminUserInfoMapper adminUserInfoMapper;

    @Override
    public Integer selectRegisterCount(Integer platformId) {
        return adminUserInfoMapper.selectRegisterCount(platformId);
    }

    @Override
    public Integer selectLastLoginTimes(Integer platformId) {
        return adminUserInfoMapper.selectLastLoginTimes(platformId);
    }

    @Override
    public Integer selectOnlineUser(Integer platformId) {
        return adminUserInfoMapper.selectOnlineUser(platformId);
    }

    @Override
    public UserVo selectUserInfo(Integer id) {
        return adminUserInfoMapper.selectUserInfo(id);
    }


    @Override
    public List<UserLoginLogoDto> selectUserLoginLog(UserLoginLogoDto userLoginLogoDto) {
        PageHelper.startPage(userLoginLogoDto.getPageNum(), userLoginLogoDto.getPageSize());
        Integer id = userLoginLogoDto.getId();
        return adminUserInfoMapper.selectUserLoginLog(id);
    }

    @Override
    public List<UserWalletDto> selectUserWallet(UserWalletDto userWalletDto) {
        PageHelper.startPage(userWalletDto.getPageNum(), userWalletDto.getPageSize());
        return adminUserInfoMapper.selectUserWallet(userWalletDto);
    }

    @Override
    public List<UserMoodDto> selectUserMood(UserMoodDto userMoodDto) {
        PageHelper.startPage(userMoodDto.getPageNum(), userMoodDto.getPageSize());
        return adminUserInfoMapper.selectUserMood(userMoodDto);
    }

    @Override
    public List<UserGroupDto> selectUsergroup(UserGroupDto userGroupDto) {
        System.out.println(userGroupDto);
        PageHelper.startPage(userGroupDto.getPageNum(), userGroupDto.getPageSize());
        return adminUserInfoMapper.selectUserGroup(userGroupDto);
    }


}
