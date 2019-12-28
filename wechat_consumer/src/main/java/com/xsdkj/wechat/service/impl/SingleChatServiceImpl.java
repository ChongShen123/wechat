package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.SingleChatService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/27 10:28
 */
@Service
public class SingleChatServiceImpl implements SingleChatService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void save(SingleChat singleChat) {
        mongoTemplate.save(singleChat);
    }
}