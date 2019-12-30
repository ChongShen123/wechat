package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.SingleChatService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 单聊消息存储
 *
 * @author tiankong
 * @date 2019/12/27 11:00
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_SINGLE_CHAT)
public class SingleChatHandler implements MsgHandler {
    @Resource
    private SingleChatService singleChatService;

    @Override
    public void execute(RabbitMessageBoxBo box) {
        singleChatService.save((SingleChat) box.getData());
    }
}
