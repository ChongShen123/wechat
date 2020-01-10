package com.xsdkj.wechat.service.impl;
import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.entity.user.User;

import com.xsdkj.wechat.mapper.AdminUserInfoMapper;
import com.xsdkj.wechat.service.AdminUserInfoService;
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
        return  adminUserInfoMapper.selectRegisterCount(platformId);

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
    public List<User> selectUserInfo(Integer id) {
        return adminUserInfoMapper.selectUserInfo(id);
    }

    @Override
    public List<UserLoginLogoDto> selectUserLoginLog(Integer id) {
        return adminUserInfoMapper.selectUserLoginLog(id);
    }


}
