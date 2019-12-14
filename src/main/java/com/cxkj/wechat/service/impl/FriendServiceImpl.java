package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.Friend;
import com.cxkj.wechat.mapper.FriendMapper;
import com.cxkj.wechat.service.FriendService;
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
