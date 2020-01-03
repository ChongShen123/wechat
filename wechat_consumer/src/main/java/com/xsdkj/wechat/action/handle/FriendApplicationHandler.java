package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 好友申请类型
 *
 * @author tiankong
 * @date 2019/12/27 19:31
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_FRIEND_APPLICATION)
public class FriendApplicationHandler implements MsgHandler {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void execute(MsgBox box) {
        FriendApplication application = (FriendApplication) box.getData();
        mongoTemplate.save(application);
    }
}