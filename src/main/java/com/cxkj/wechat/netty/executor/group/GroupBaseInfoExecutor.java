package com.cxkj.wechat.netty.executor.group;

import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.vo.GroupBaseInfoVO;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/15 13:58
 */
@ExecutorAnno(command = Command.GROUP_BASE_INFO)
@Service
public class GroupBaseInfoExecutor extends ChatExecutor {
    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        GroupBaseInfoVO groupBaseInfoVO = groupService.getBaseInfo(param.getGroupId());
        if (groupBaseInfoVO != null) {
            sendMessage(channel, JsonResult.success(groupBaseInfoVO, command));
        } else {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, command));
        }
    }
}
