package com.cxkj.wechat.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/10 13:28
 */
@RestController
@RequestMapping("/rabbit")
public class TestRabbitMQController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/direct/{str}")
    public void directTest(@PathVariable("str") String str) {
        rabbitTemplate.convertAndSend("hello-queue", str);
    }
}
