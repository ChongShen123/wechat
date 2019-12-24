package com.cxkj.wechat.netty.base;

import com.cxkj.wechat.util.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Http升级WebSocket
 * 该类主要作用：完成HTTP协议升级到WebSocket协议
 *
 * @author tiankong
 * @date 2019/11/17 18:52
 */
@Component
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * 读取完连接的消息后，对消息进行处理。
     * 这里仅处理HTTP请求，WebSocket请求交给下一个处理器
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handlerHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            ctx.fireChannelRead(((WebSocketFrame) msg).retain());
        }
    }

    /**
     * 处理HTTP请求，主要是完成HTTP协议到WebSocket协议的升级
     */
    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // TODO 这段代码不明白需要看下
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws/" + ctx.channel() + "/websocket", null, false);
        WebSocketServerHandshaker handShaker = factory.newHandshaker(request);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            SessionUtil.WEB_SOCKET_SERVER_HAND_SHAKER.put(ctx.channel().id().asLongText(), handShaker);
            try {
                handShaker.handshake(ctx.channel(), request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 响应客户端
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, DefaultFullHttpResponse response) {
        if (response.status().code() != HttpStatus.OK.value()) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (!keepAlive) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
