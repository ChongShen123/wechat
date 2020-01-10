package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.mapper.UserScoreMapper;
import com.xsdkj.wechat.service.UserScoreService;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.UserScoreVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/6 15:36
 */
@Service
public class UserScoreServiceImpl implements UserScoreService {
    @Resource
    private UserScoreMapper userScoreMapper;
    @Resource
    private UserUtil userUtil;

    @Override
    public UserScoreVo getUserScore() {
        User user = userUtil.currentUser().getUser();
        return userScoreMapper.getUserScoreVoByUid(user.getId());
    }
}
