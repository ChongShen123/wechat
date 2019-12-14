package com.cxkj.wechat.netty.handler;

import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.Attributes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * 登录检测器
 *
 * @author tiankong
 * @date 2019/11/18 17:40
 */
@ChannelHandler.Sharable
@Component
public class CheckLoginHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        Session session = ctx.channel().attr(Attributes.SESSION).get();
        if (session == null) {
            ctx.channel().attr(Attributes.IS_LOGIN).set(false);
        }
    }
}
