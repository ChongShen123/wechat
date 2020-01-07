package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.vo.GroupBaseInfoVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/15 13:58
 */
@Slf4j
@CmdAnno(cmd = Cmd.GROUP_BASE_INFO)
@Service
public class GroupInfoBaseCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        GroupBaseInfoVo groupBaseInfoVO = groupService.getBaseInfo(requestParam.getGroupId());
        if (groupBaseInfoVO != null) {
            sendMessage(channel, JsonResult.success(groupBaseInfoVO, cmd));
        } else {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST, cmd));
        }
        log.debug("查看群基本信息处理完成 {}ms", DateUtil.spendMs(begin));
    }
}
