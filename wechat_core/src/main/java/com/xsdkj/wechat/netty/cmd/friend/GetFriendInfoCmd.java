package com.xsdkj.wechat.netty.cmd.friend;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.DataEmptyException;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.vo.GetFriendInfoVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 查看用户详情
 *
 * @author tiankong
 * @date 2019/12/30 19:00
 */
@Slf4j
@CmdAnno(cmd = Cmd.GET_FRIEND_INFO)
@Component
public class GetFriendInfoCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setUserId(Integer.parseInt(parseParam(param, ParamConstant.KEY_USER_ID)));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        log.debug("开始处理查询好友详情工作...");
        Integer userId = requestParam.getUserId();
        User user = userService.getRedisUserByUserId(userId);
        if (user == null) {
            throw new DataEmptyException();
        }
        GetFriendInfoVo getFriendInfoVo = new GetFriendInfoVo();
        BeanUtils.copyProperties(user, getFriendInfoVo);
        sendMessage(channel, JsonResult.success(getFriendInfoVo, cmd));
        log.debug("好友详情查询完毕 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
    }
}
