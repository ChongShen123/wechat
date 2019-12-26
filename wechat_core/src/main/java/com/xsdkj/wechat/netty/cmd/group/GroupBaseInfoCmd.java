package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.vo.GroupBaseInfoVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/15 13:58
 */
@CmdAnno(cmd = Cmd.GROUP_BASE_INFO)
@Service
public class GroupBaseInfoCmd extends BaseChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        GroupBaseInfoVo groupBaseInfoVO = groupService.getBaseInfo(requestParam.getGroupId());
        if (groupBaseInfoVO != null) {
            sendMessage(channel, JsonResult.success(groupBaseInfoVO, cmd));
        } else {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, cmd));
        }
    }
}
