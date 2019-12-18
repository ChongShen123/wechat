package com.cxkj.wechat.netty.base;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Attributes;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.netty.executor.ExecutorManager;
import com.cxkj.wechat.netty.executor.base.BaseHandler;
import com.cxkj.wechat.netty.executor.base.Executor;
import com.cxkj.wechat.netty.executor.base.RegisterExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 业务处理器
 *
 * @author tiankong
 * @date 2019/11/17 18:51
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Resource(name = "executorManager")
    private ExecutorManager commandManager;
    @Resource
    private CheckLoginHandler checkLoginHandler;

    /**
     * 读取完连接的消息后，对消息进行处理
     * 这里主要是处理WebSocket请求。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx, msg);
    }

    /**
     * 处理WebSocketFrame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handShaker = SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.get(ctx.channel().id().asLongText());
            if (handShaker == null) {
                BaseHandler.sendMessage(ctx.channel(), JsonResult.failed(ResultCodeEnum.CONNECTION_NOT_EXIST));
            } else {
                handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            }
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            BaseHandler.sendMessage(ctx.channel(), JsonResult.failed(ResultCodeEnum.MESSAGE_FORMAT_ERROR));
            return;
        }
        // 客户端发送过来的消息
        String request = ((TextWebSocketFrame) frame).text();
        JSONObject param;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            BaseHandler.sendMessage(ctx.channel(), JsonResult.failed(ResultCodeEnum.STRING_CONVERSION_ERROR));
            e.printStackTrace();
            return;
        }
        Integer command = param.getInteger(SystemConstant.KEY_COMMAND);
        Executor executor = commandManager.getCommand(command);
        if (executor == null) {
            BaseHandler.sendMessage(ctx.channel(), JsonResult.failed(ResultCodeEnum.COMMAND_NOT_EXIST));
            return;
        }
        if (executor instanceof RegisterExecutor) {
            executor.execute(param, ctx.channel());
            return;
        }
        // 检测用户是否登录
        checkLoginHandler.channelRead0(ctx, frame);
        if (null != ctx.channel().attr(Attributes.IS_LOGIN).get()) {
            BaseHandler.sendMessage(ctx.channel(), JsonResult.failed(ResultCodeEnum.UNAUTHORIZED));
            BaseHandler.remove(ctx.channel());
            return;
        }
        executor.execute(param, ctx.channel());
    }


    /**
     * 客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.remove(ctx.channel().id().asLongText());
        SessionBo session = ctx.channel().attr(Attributes.SESSION).get();
        if (session == null) {
            ctx.channel().close();
            return;
        }
        SessionUtil.ONLINE_USER_MAP.remove(session.getUserId());
        log.info("已移除握手实例,当前握手实例总数为:{}", SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.size());
        log.info("userId为{}的用户已经退出聊天,当前在线人数为{}", ctx.channel().attr(Attributes.SESSION).get().getUserId(), SessionUtil.ONLINE_USER_MAP.size());
        ctx.channel();
    }

    /**
     * 异常处理： 关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.remove(channel.id().asLongText());
        SessionBo session = channel.attr(Attributes.SESSION).get();
        if (session != null) {
            SessionUtil.ONLINE_USER_MAP.remove(session.getUserId());
        }
        cause.printStackTrace();
        ctx.close();
    }
}
