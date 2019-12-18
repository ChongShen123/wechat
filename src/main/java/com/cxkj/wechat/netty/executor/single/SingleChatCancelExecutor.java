package com.cxkj.wechat.netty.executor.single;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.netty.ex.ValidateException;
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
    protected void parseParam(JSONObject param) {
        try {
            Integer toUserId = param.getInteger(SystemConstant.KEY_TO_USER_ID);
            String singleChatId = param.getString(SystemConstant.KEY_ID);
            requestParam.setToUserId(toUserId);
            requestParam.setId(singleChatId);
        } catch (Exception e) {
            throw new ValidateException();
        }

    }

    @Override
    protected void concreteAction(Channel channel) {
        Channel userChannel = SessionUtil.getUserChannel(requestParam.getToUserId());
        SingleChat singleChat = singleChatService.getById(requestParam.getId());
        if (singleChat != null && System.currentTimeMillis() < singleChat.getCreateTimes() + SystemConstant.CHAT_CANCEL_TIMES) {
            if (userChannel != null) {
                sendMessage(userChannel, JsonResult.success(new SingleChatCancelVo(requestParam.getId()), command));
            }
            singleChatService.deleteById(requestParam.getId());
        }else {
            sendMessage(channel,JsonResult.failed(ResultCodeEnum.UNDO_TIME_EXCEEDED));
        }
    }
}
