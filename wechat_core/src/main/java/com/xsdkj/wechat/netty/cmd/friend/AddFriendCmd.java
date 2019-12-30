package com.xsdkj.wechat.netty.cmd.friend;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.entity.chat.User;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.service.ex.ValidateException;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.FriendApplicationVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/12 13:22
 */
@Service
@Slf4j
@CmdAnno(cmd = Cmd.ADD_FRIEND)
public class AddFriendCmd extends BaseChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        String username = param.getString(ParamConstant.KEY_USERNAME);
        String content = param.getString(ParamConstant.KEY_CONTENT);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(content)) {
            throw new ValidateException();
        }
        requestParam.setUsername(username);
        requestParam.setContent(content);
    }

    @Override
    protected void concreteAction(Channel channel) {
        String username = requestParam.getUsername();
        String content = requestParam.getContent();
        User friend = userService.getByUsername(username);
        if (friend == null) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_FOND, cmd));
            return;
        }
        if (friendApplicationService.countByToUserIdAndFromUserId(friend.getId(), session.getUid()) > 0) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.REPEAT_EXCEPTION));
            return;
        }
        FriendApplication application = createFriendApplication(session, friend.getId(), content, ChatConstant.ADD_FRIEND);
        Channel friendChannel = SessionUtil.getUserChannel(userService.getByUsername(username).getId());
        application.setRead(friendChannel != null);
        if (friendChannel != null) {
            sendMessage(friendChannel, JsonResult.success(new FriendApplicationVo(application), Cmd.ADD_FRIEND));
        }
        // 放入消息队列
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(RabbitConstant.BOX_TYPE_FRIEND_APPLICATION, application));
        sendMessage(channel, JsonResult.success(ChatConstant.MSG_SUCCESS, cmd));
    }
}
