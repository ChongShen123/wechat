package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.service.GroupChatService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/27 10:27
 */
@Service
public class GroupChatServiceImpl implements GroupChatService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void save(GroupChat groupChat) {
        System.out.println(groupChat);
        mongoTemplate.save(groupChat);
    }
}
