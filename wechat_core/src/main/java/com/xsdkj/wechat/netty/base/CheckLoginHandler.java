package com.xsdkj.wechat.netty.base;

import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.constant.Attributes;
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
        SessionBo session = ctx.channel().attr(Attributes.SESSION).get();
        if (session == null) {
            ctx.channel().attr(Attributes.IS_LOGIN).set(false);
        }
    }
}
