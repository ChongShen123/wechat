package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MessageHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.service.GroupChatService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 单聊消息存储
 *
 * @author tiankong
 * @date 2019/12/27 11:00
 */
@Component
@SaveAnno(type = SystemConstant.BOX_TYPE_SINGLE_CHAT)
public class SingleChatHandler implements MessageHandler {
    @Resource
    private GroupChatService groupChatService;

    @Override
    public void execute(RabbitMessageBoxBo box) {
        groupChatService.save((GroupChat) box.getData());
    }
}
