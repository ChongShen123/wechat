package com.xsdkj.wechat.netty.cmd.friend;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.vo.UserFriendVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/25 17:10
 */
@CmdAnno(cmd = Cmd.LIST_FRIEND)
@Service
public class ListUserFriendCmd extends BaseChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
    }

    @Override
    protected void concreteAction(Channel channel) {
        List<UserFriendVo> listUserFriendVos = userService.listFriendByUid(session.getUid());
        sendMessage(channel, JsonResult.success(listUserFriendVos, cmd));
    }
}
