package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.vo.ListMembersVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/17 11:12
 */
@Service
@ExecutorAnno(command = Command.LIST_GROUP_MEMBERS)
public class ListMembersExecutor extends ChatExecutor {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Integer groupId = requestParam.getGroupId();
        List<ListMembersVo> listMembersVos = groupService.listGroupMembersByGroupId(groupId);
        if (listMembersVos.size() > 0) {
            sendMessage(channel, JsonResult.success(listMembersVos, command));
        }
    }
}
