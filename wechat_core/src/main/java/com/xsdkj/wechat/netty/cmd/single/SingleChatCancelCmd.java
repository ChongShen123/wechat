package com.xsdkj.wechat.netty.cmd.single;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.ex.DataEmptyException;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.SingleChatCancelVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 单聊消息撤销
 *
 * @author tiankong
 * @date 2019/12/13 16:17
 */
@Slf4j
@Service
@CmdAnno(cmd = Cmd.SINGLE_CHAT_CANCEL)
public class SingleChatCancelCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        try {
            log.debug("开始解析参数...");
            Integer toUserId = param.getInteger(ParamConstant.KEY_TO_USER_ID);
            log.debug("toUserId:{}", toUserId);
            String singleChatId = param.getString(ParamConstant.KEY_ID);
            log.debug("singleChatId:{}", singleChatId);
            requestParam.setToUserId(toUserId);
            requestParam.setId(singleChatId);
        } catch (Exception e) {
            throw new ValidateException();
        }

    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理撤销工作...");
        Integer toUserId = requestParam.getToUserId();
        Channel userChannel = SessionUtil.getUserChannel(toUserId);
        String singleChatId = requestParam.getId();
        SingleChat singleChat = singleChatService.getById(singleChatId);
        if (singleChat != null) {
            log.debug("获取单聊消息:{} {}ms", singleChat, DateUtil.spendMs(begin));
            if (System.currentTimeMillis() < singleChat.getCreateTimes() + SystemConstant.CHAT_CANCEL_TIMES) {
                if (userChannel != null) {
                    sendMessage(userChannel, JsonResult.success(new SingleChatCancelVo(singleChatId), cmd));
                    log.debug("已通知对方需要撤销的消息 {}ms", DateUtil.spendMs(begin));
                }
                singleChatService.deleteById(singleChatId);
                sendMessage(channel, JsonResult.success(cmd));
                log.debug("撤销工作完毕 {}ms", DateUtil.spendMs(begin));
                return;
            }
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.UNDO_TIME_EXCEEDED));
            return;
        }
        log.debug("单聊消息{}不存在 {}ms", singleChatId,DateUtil.spendMs(begin));
        log.debug("撤销工作完毕 {}ms", DateUtil.spendMs(begin));
        throw new DataEmptyException();
    }
}

