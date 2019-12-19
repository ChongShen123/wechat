package com.cxkj.wechat.netty.executor.base;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.GroupInfoBo;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Attributes;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.util.ThreadUtil;
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
            log.info("已移除握手实例");
            if (session == null) {
                channel.close();
                return;
            }
            SessionUtil.ONLINE_USER_MAP.remove(session.getUserId());
            log.info("已移除握手实例,当前握手实例总数为:{}", SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.size());
            log.info("userId为{}的用户已经退出聊天,当前在线人数为{}", channel.attr(Attributes.SESSION).get().getUserId(), SessionUtil.ONLINE_USER_MAP.size());
            channel.close();
        });
//        Iterator<Map.Entry<Integer, Channel>> iterator = SessionUtil.onlineUserMap.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Integer, Channel> next = iterator.next();
//            if (next.getValue() == ctx.channel()) {
//                log.info("正在移除握手实例...");
//                SessionUtil.webSocketServerHandshakerMap.remove(ctx.channel().id().asLongText());
//                log.info("已移除握手实例,当前握手实例总数为:{}", SessionUtil.webSocketServerHandshakerMap.size());
//                iterator.remove();
//                log.info("userId为{}的用户已经退出聊天,当前在线人数为{}", next.getKey(), SessionUtil.onlineUserMap.size());
//                break;
//            }
//        }
    }
}
