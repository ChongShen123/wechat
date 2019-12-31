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
     * @param exchangeName 交换器名称 RabbitConstant.FANOUT_SERVICE_NAME/FANOUT_CHAT_NAME
     * @param box          box  RabbitMessageBoxBo.createBox(type,data)
     */
    void addExchange(String exchangeName, RabbitMessageBoxBo box);
}
