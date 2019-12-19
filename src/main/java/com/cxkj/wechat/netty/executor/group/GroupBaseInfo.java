package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.vo.GroupBaseInfoVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/15 13:58
 */
@ExecutorAnno(command = Command.GROUP_BASE_INFO)
@Service
public class GroupBaseInfo extends ChatExecutor {

    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        GroupBaseInfoVo groupBaseInfoVO = groupService.getBaseInfo(requestParam.getGroupId());
        if (groupBaseInfoVO != null) {
            sendMessage(channel, JsonResult.success(groupBaseInfoVO, command));
        } else {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, command));
        }
    }
}
