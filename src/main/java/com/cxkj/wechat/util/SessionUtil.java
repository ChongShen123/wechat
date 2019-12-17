package com.cxkj.wechat.util;

import com.cxkj.wechat.bo.GroupInfoBo;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiankong
 * @date 2019/11/17 19:22
 */
@Component
public class SessionUtil {
    /**
     * 握手实例
     */
    public static Map<String, WebSocketServerHandshaker> WEB_SOCKET_SERVER_HAND_SHAKER = new ConcurrentHashMap<>();

    /**
     * 所有群
     */
    public static Map<Integer, GroupInfoBo> GROUP_MAP = new ConcurrentHashMap<>();

    /**
     * 在线用户
     */
    public static Map<Integer, Channel> ONLINE_USER_MAP = new ConcurrentHashMap<>();

    // -------------------------------------基本----------------------------------------------------

    /**
     * 获取用户channel
     */
    public static Channel getUserChannel(Integer uid) {
        return ONLINE_USER_MAP.get(uid);
    }

    /**
     * 注册用户channel
     */
    public static void registerUserChannel(Integer uid, Channel channel) {
        ONLINE_USER_MAP.put(uid, channel);
    }

    public static SessionBo getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    // --------------------------------群相关-----------------------------------

    /**
     * 加入群组
     */
    public static void joinGroup(Integer groupId, Channel channel) {
        GROUP_MAP.get(groupId).getChannelGroup().add(channel);
    }

    /**
     * 退出群组
     */
    public static void quitGroup(Integer groupId, Channel channel) {
        GROUP_MAP.get(groupId).getChannelGroup().remove(channel);
    }

    /**
     * 获取群组ChannelGroup
     */
    public static ChannelGroup getChannelGroup(Integer groupId) {
        return GROUP_MAP.get(groupId).getChannelGroup();
    }


    /**
     * 获取群组用户ID集合
     */
    public static List<Integer> listGroupMemberIds(Integer groupId) {
        List<Integer> list = new ArrayList<>();
        getChannelGroup(groupId).forEach(channel -> {
            if (channel != null) {
                list.add(getSession(channel).getUserId());
            }
        });
        return list;
    }

}
