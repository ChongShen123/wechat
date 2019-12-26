package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.vo.ListMembersVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/17 11:12
 */
@Service
@CmdAnno(cmd = Cmd.LIST_GROUP_MEMBERS)
public class ListMembersCmd extends BaseChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Integer groupId = requestParam.getGroupId();
        List<ListMembersVo> listMembersVos = groupService.listGroupMembersByGroupId(groupId);
        if (listMembersVos.size() > 0) {
            sendMessage(channel, JsonResult.success(listMembersVos, cmd));
        }
    }
}
