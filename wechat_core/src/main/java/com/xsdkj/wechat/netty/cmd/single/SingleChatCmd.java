package com.xsdkj.wechat.netty.cmd.single;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.ex.UserNotFountException;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/13 11:34
 */
@Slf4j
@Service
@CmdAnno(cmd = Cmd.SINGLE_CHAT)
public class SingleChatCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        Integer toUserId = param.getInteger(ParamConstant.KEY_TO_USER_ID);
        String content = param.getString(ParamConstant.KEY_CONTENT);
        Byte type = param.getByte(ParamConstant.KEY_TYPE);
        if (ObjectUtil.isEmpty(toUserId) || ObjectUtil.isEmpty(content) || ObjectUtil.isEmpty(type)) {
            throw new ValidateException();
        }
        requestParam.setToUserId(toUserId);
        requestParam.setContent(content);
        requestParam.setByteType(type);
    }

    @Override
    protected void concreteAction(Channel channel) {
        log.debug(LogUtil.INTERVAL);
        log.debug("开始处理用户{}的单聊信息...", session.getUid());
        long begin = System.currentTimeMillis();
        Integer toUserId = requestParam.getToUserId();
        Integer uid = session.getUid();
        String content = requestParam.getContent();
        Byte singleChatType = requestParam.getByteType();
        SingleChat chat = chatUtil.createNewSingleChat(toUserId, uid, content, singleChatType);
        // 获取一条消息
        Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(requestParam.getToUserId());
        try {
            if (toUserChannel != null) {
                chat.setRead(true);
                log.debug("本条信息为:{}{}ms", chat, DateUtil.spendMs(begin));
                sendMessage(toUserChannel, JsonResult.success(chat, cmd));
                log.debug("信息已发送给两个客户端:{}ms", DateUtil.spendMs(begin));
            } else {
                User toUser = userService.getRedisUserByUserId(toUserId);
                if (toUser == null) {
                    log.debug("用户{}不存在", toUserId);
                    throw new UserNotFountException();
                }
                chat.setRead(false);
                log.debug("用户{}不在线:{}ms", toUserId, DateUtil.spendMs(begin));
            }
            sendMessage(channel, JsonResult.success(chat, cmd));
            singleChatService.save(chat);
            log.debug("信息已保存数据库:{}ms", DateUtil.spendMs(begin));
        } finally {
            log.debug("消息处理完成:{}ms", DateUtil.spendMs(begin));
        }
    }
}
