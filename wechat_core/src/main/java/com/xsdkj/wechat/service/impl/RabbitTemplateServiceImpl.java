package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.service.RabbitTemplateService;
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
    public void addExchange(String exchangeName, RabbitMessageBoxBo box) {
        rabbitTemplate.convertAndSend(exchangeName, null, box);
    }
}
