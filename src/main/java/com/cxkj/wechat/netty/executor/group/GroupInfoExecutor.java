package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.vo.GroupInfoVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 查看群详情
 *
 * @author tiankong
 * @date 2019/12/15 14:31
 */
@ExecutorAnno(command = Command.GROUP_INFO)
@Service
public class GroupInfoExecutor extends ChatExecutor {

    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        GroupInfoVo groupInfo = groupService.getGroupInfo(requestParam.getGroupId());
        sendMessage(channel, JsonResult.success(groupInfo, command));
    }
}
