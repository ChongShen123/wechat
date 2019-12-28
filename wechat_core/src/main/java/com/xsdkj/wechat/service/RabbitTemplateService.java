package com.xsdkj.wechat.service;


import com.xsdkj.wechat.bo.RabbitMessageBoxBo;

/**
 * @author tiankong
 * @date 2019/12/13 13:55
 */
public interface RabbitTemplateService {
    /**
     * 消息进入队列
     *
     * @param exchangeName 交换器名称
     * @param box          box
     */
    void addExchange(String exchangeName, RabbitMessageBoxBo box);
}
