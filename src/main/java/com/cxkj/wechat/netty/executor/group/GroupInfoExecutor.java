package com.cxkj.wechat.netty.executor.group;

import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.vo.GroupInfoVO;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/15 14:31
 */
@ExecutorAnno(command = Command.GROUP_INFO)
@Service
public class GroupInfoExecutor extends ChatExecutor {
    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        GroupInfoVO groupInfo = groupService.getGroupInfo(param.getGroupId());
        sendMessage(channel, JsonResult.success(groupInfo, command));
    }
}
