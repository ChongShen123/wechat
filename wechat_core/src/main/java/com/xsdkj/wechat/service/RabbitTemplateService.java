package com.xsdkj.wechat.service;


import com.xsdkj.wechat.bo.RabbitMessageBoxBo;

/**
 * @author tiankong
 * @date 2019/12/13 13:55
 */
public interface RabbitTemplateService {
    /**
     * 单聊消息进入队列
     *
     * @param queue 队列名称
     * @param box   内容
     */
    void addChatInfo(String queue, RabbitMessageBoxBo box);
}
