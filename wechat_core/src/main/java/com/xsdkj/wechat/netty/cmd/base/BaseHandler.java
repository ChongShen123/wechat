package com.xsdkj.wechat.netty.cmd.base;

import com.alibaba.fastjson.JSONObject;

import com.xsdkj.wechat.bo.GroupInfoBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.Attributes;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.util.ThreadUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 聊天室基础类
 *
 * @author tiankong
 * @date 2019/11/18 14:24
 */
@Component
@Slf4j
public class BaseHandler {
    /**
     * 单聊信息
     *
     * @param channel    channel
     * @param jsonResult jsonResult
     */
    public static void sendMessage(Channel channel, JsonResult jsonResult) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(jsonResult)));
    }

    /**
     * 群聊消息
     *
     * @param gid        group id
     * @param jsonResult 信息
     */
    protected static void sendGroupMessage(Integer gid, JsonResult jsonResult) {
        GroupInfoBo groupInfo = SessionUtil.GROUP_MAP.get(gid);
        if (groupInfo != null) {
            groupInfo.getChannelGroup().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(jsonResult)));
        }
    }

    /**
     * 退出聊天
     *
     * @param channel channel
     */
    public static void remove(Channel channel) {
        ThreadUtil.getSingleton().submit(() -> {
            SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.remove(channel.id().asLongText());
            SessionBo session = channel.attr(Attributes.SESSION).get();
            SessionUtil.ONLINE_USER_MAP.remove(session.getUid());
            log.info("已移除握手实例,当前握手实例总数为:{}", SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.size());
            log.info("userId为{}的用户已经退出聊天,当前在线人数为{}", channel.attr(Attributes.SESSION).get().getUid(), SessionUtil.ONLINE_USER_MAP.size());
            channel.close();
        });
    }
}
