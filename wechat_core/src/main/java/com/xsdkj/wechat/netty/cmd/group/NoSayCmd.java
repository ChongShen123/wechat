package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/28 16:19
 */
@CmdAnno(cmd = Cmd.NO_SAY)
@Service
public class NoSayCmd extends BaseChatCmd {
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
        List<Integer> adminIds = groupService.listGroupManagerByUserId(groupId);
        if (!adminIds.contains(session.getUid())) {
            throw new PermissionDeniedException();
        }
        groupService.saveNoSay(userId, groupId, times);
        groupService.updateRedisNoSayData();
        sendMessage(channel, JsonResult.success(cmd));
    }
}
