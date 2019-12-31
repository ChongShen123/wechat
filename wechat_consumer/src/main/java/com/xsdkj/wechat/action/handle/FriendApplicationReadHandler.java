package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.service.FriendApplicationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新好友申请状态为已读
 *
 * @author tiankong
 * @date 2019/12/31 12:35
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_FRIEND_APPLICATION_READ)
public class FriendApplicationReadHandler implements MsgHandler {
    @Resource
    private FriendApplicationService friendApplicationService;
    @Override
    public void execute(RabbitMessageBoxBo box) {
        List<FriendApplication> friendApplications = (List<FriendApplication>) box.getData();
        List<String> ids = new ArrayList<>();
        friendApplications.forEach(friendApplication -> ids.add(friendApplication.getId()));
        friendApplicationService.updateFriendApplicationRead(true, ids);
    }
}
