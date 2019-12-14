package com.cxkj.wechat.netty.executor.single;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.service.FriendApplicationService;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.util.ThreadUtil;
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
    public void execute(JSONObject param, Channel channel) {
        ThreadUtil.getSingleton().submit(() -> {
            try {
                String id;
                Integer uid;
                id = param.getString(SystemConstant.KEY_ID);
                uid = param.getInteger(SystemConstant.KEY_TO_USER_ID);
                if (id == null || uid == null) {
                    throw new RuntimeException();
                }
                Channel userChannel = SessionUtil.getUserChannel(uid);
                if (userChannel != null) {
                    sendMessage(userChannel, JsonResult.success(new SingleChatCancelVo(id), command));
                }
                friendApplicationService.deleteById(id);
            } catch (Exception e) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, command));
            }
        });
    }
}
