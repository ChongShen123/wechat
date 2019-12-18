package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.ChatInfo;
import com.cxkj.wechat.service.RabbitTemplateService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/13 13:58
 */
@Service
public class RabbitTemplateServiceImpl implements RabbitTemplateService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void addChatInfo(String queue, ChatInfo content) {
        rabbitTemplate.convertAndSend(queue, content);
    }
}
