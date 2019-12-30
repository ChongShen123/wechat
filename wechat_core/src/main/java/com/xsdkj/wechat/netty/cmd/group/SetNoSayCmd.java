package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 设置用户禁言
 *
 * @author tiankong
 * @date 2019/12/28 16:19
 */
@CmdAnno(cmd = Cmd.NO_SAY)
@Service
public class SetNoSayCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
        parseUserId(param);
        parseTimes(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer groupId = requestParam.getGroupId();
        Integer userId = requestParam.getUserId();
        Long times = requestParam.getTimes();
        if (!checkAdmin(groupId, session.getUid())) {
            throw new PermissionDeniedException();
        }
        groupService.saveNoSay(userId, groupId, times);
        groupService.updateRedisNoSayData();
        sendMessage(channel, JsonResult.success(cmd));
    }


}
