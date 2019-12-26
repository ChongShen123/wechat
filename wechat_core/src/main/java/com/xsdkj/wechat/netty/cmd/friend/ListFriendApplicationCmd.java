package com.xsdkj.wechat.netty.cmd.friend;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查看好友申请列表
 *
 * @author tiankong
 * @date 2019/12/12 17:26
 */
@Service
@CmdAnno(cmd = Cmd.LIST_FRIEND_APPLICATION)
public class ListFriendApplicationCmd extends BaseChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
    }

    @Override
    protected void concreteAction(Channel channel) {
        List<FriendApplication> friendApplicationList = friendApplicationService.listByUserId(SessionUtil.getSession(channel).getUid());
        sendMessage(channel, JsonResult.success(friendApplicationList, cmd));
    }
}
