package com.cxkj.wechat.netty.executor.single;

import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.service.FriendApplicationService;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.SingleChatCancelVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 单聊消息撤销
 *
 * @author tiankong
 * @date 2019/12/13 16:17
 */
@Service
@ExecutorAnno(command = Command.SINGLE_CHAT_CANCEL)
public class SingleChatCancelExecutor extends ChatExecutor {
    @Resource
    private FriendApplicationService friendApplicationService;

    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        Channel userChannel = SessionUtil.getUserChannel(param.getUserId());
        if (userChannel != null) {
            sendMessage(userChannel, JsonResult.success(new SingleChatCancelVo(param.getId()), command));
        }
        friendApplicationService.deleteById(param.getId());
    }
}
