package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.SingleChatService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 更新单聊消息为已读
 * @author tiankong
 * @date 2019/12/31 11:28
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_SINGLE_CHAT_READ)
public class SingleChatReadHandler implements MsgHandler {
    @Resource
    private SingleChatService singleChatService;

    @Override
    public void execute(MsgBox box) {
        List<SingleChat> singleChats = (List<SingleChat>) box.getData();
        singleChatService.updateRead(true, singleChats);
    }
}
