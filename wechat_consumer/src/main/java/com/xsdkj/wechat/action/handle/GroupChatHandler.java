package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MessageHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.SystemConstant;
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
@SaveAnno(type = SystemConstant.BOX_TYPE_GROUP_CHAT)
public class GroupChatHandler implements MessageHandler {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void execute(RabbitMessageBoxBo box) {
        mongoTemplate.save(box.getData());
    }
}
