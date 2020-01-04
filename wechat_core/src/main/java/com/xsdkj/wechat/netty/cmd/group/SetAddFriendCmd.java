package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * @author tiankong
 * @date 2020/1/2 16:05
 */
@Component
@CmdAnno(cmd = Cmd.SET_GROUP_ADD_FRIEND)
public class SetAddFriendCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
        requestParam.setAddFriend(Boolean.parseBoolean(parseParam(param, ParamConstant.KEY_ADD_FRIEND)));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer groupId = requestParam.getGroupId();
        Boolean addFriend = requestParam.getAddFriend();
        if (!checkAdmin(groupId, session.getUid())) {
            throw new PermissionDeniedException();
        }
        groupService.updateAddFriend(groupId, addFriend);
        groupService.updateRedisGroupById(groupId);
        sendMessage(channel, JsonResult.success(cmd));
    }
}
