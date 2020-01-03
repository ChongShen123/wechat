package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.service.GroupChatService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 群组消息储存
 *
 * @author tiankong
 * @date 2019/12/27 11:02
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_GROUP_CHAT)
public class GroupChatHandler implements MsgHandler {
    @Resource
    private GroupChatService groupChatService;
    @Override
    public void execute(RabbitMessageBoxBo box) {
        groupChatService.save((GroupChat) box.getData());
    }
}
