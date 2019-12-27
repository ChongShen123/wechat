package com.xsdkj.wechat.action;

import com.xsdkj.wechat.bo.RabbitMessageBoxBo;

/**
 * 消息处理者抽象接口
 *
 * @author tiankong
 * @date 2019/12/27 10:41
 */
public interface MessageHandler {
    /**
     * 执行方法
     *
     * @param box 消息
     */
    void execute(RabbitMessageBoxBo box);

}
