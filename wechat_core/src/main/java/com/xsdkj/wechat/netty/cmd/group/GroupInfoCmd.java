package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.vo.GroupInfoVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 查看群详情
 *
 * @author tiankong
 * @date 2019/12/15 14:31
 */
@CmdAnno(cmd = Cmd.GROUP_INFO)
@Service
public class GroupInfoCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        GroupInfoVo groupInfo = groupService.getGroupInfo(requestParam.getGroupId());
        sendMessage(channel, JsonResult.success(groupInfo, cmd));
    }
}
