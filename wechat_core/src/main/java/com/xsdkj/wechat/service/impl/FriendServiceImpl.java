package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.Friend;
import com.xsdkj.wechat.mapper.FriendMapper;
import com.xsdkj.wechat.service.FriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 18:59
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Resource
    private FriendMapper friendMapper;

    @Override
    public void saveList(List<Friend> friends) {
        friendMapper.saveList(friends);
    }
}
