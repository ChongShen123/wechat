package com.cxkj.wechat.netty.base;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Channel管理类
 *
 * @author tiankong
 * @date 2019/11/17 18:51
 */
@Component
public class ChannelManager extends ChannelInitializer<SocketChannel> {
    @Resource
    private ChannelHandler httpRequestHandler;
    @Resource
    private ChannelHandler webSocketHandler;
    @Resource
    private IMIdleStateAdapter imIdleStateAdapter;

    @Value("${netty.heartbeat}")
    private Integer heartbeat;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 心跳机制
        pipeline.addLast("imIdleStateHandler", new IdleStateHandler(heartbeat, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("htp-execute", httpRequestHandler);
        pipeline.addLast("webSocket-execute", webSocketHandler);
        pipeline.addLast("imIdleStateAdapter", imIdleStateAdapter);
    }
}
