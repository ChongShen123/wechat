package com.xsdkj.wechat.test.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author tiankong
 * @date 2020/1/7 9:42
 */
public class NettyClient {
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        boot.option(ChannelOption.SO_KEEPALIVE, true)
                .group(group)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpClientCodec(),
                                new HttpObjectAggregator(1024 * 1024 * 10));
                        p.addLast("hookedHandler", new WebSocketClientHandler());
                    }
                });
        URI url = new URI("ws://localhost:9012");
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        //进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(url, WebSocketVersion.V13, null, true, httpHeaders);
        System.out.println("connect");
        final Channel channel = boot.connect(url.getHost(), url.getPort()).sync().channel();
        WebSocketClientHandler handler = (WebSocketClientHandler) channel.pipeline().get("hookedHandler");
        handler.setHandshaker(handshaker);
        handshaker.handshake(channel);
        //阻塞等待是否握手成功
        handler.handshakeFuture().sync();
        // TODO  netty服务器性能 测试
//        Thread text = new Thread(() -> {
//            int i = 30;
//            while (i > 0) {
//                System.out.println("text send");
//                TextWebSocketFrame frame = new TextWebSocketFrame("我是文本");
//                channel.writeAndFlush(frame).addListener((ChannelFutureListener) channelFuture -> {
//                    if (channelFuture.isSuccess()) {
//                        System.out.println("text send success");
//                    } else {
//                        System.out.println("text send failed  " + channelFuture.cause().getMessage());
//                    }
//                });
//            }
//
//        });
//        text.start();
    }
}
