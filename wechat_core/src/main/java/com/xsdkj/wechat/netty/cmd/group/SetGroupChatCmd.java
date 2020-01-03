package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.ex.DataEmptyException;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 设置群是否可聊天
 *
 * @author tiankong
 * @date 2019/12/30 12:37
 */
@Component
@CmdAnno(cmd = Cmd.SET_GROUP_CHAT)
public class SetGroupChatCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) throws Exception {
        parseGroupId(param);
        parseIntegerType(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer groupId = requestParam.getGroupId();
        Integer type = requestParam.getIntType();
        UserGroup group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new DataEmptyException();
        }
        if (!checkAdmin(groupId, session.getUid())) {
            throw new PermissionDeniedException();
        }
        groupService.setGroupChat(groupId, type);
        groupService.updateRedisGroupById(groupId);
        sendMessage(channel, JsonResult.success(cmd));
    }
}
