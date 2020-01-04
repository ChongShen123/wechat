package com.xsdkj.wechat.netty.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.DataEmptyException;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import com.xsdkj.wechat.vo.UserVo;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author tiankong
 * @date 2019/12/30 14:39
 */
@Component
@CmdAnno(cmd = Cmd.USER_INFO)
public class UserInfoCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setUserId(Integer.parseInt(parseParam(param, ParamConstant.KEY_USER_ID)));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer userId = requestParam.getUserId();
        User user = userService.getRedisUserByUserId(userId);
        if (user == null) {
            throw new DataEmptyException();
        }
        if (!user.getPlatformId().equals(session.getPlatformId()) || user.getType().equals(UserConstant.TYPE_USER)) {
            throw new PermissionDeniedException();
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        sendMessage(channel, JsonResult.success(userVo, cmd));
    }
}
