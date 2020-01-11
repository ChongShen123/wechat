package com.xsdkj.wechat.netty;


import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.bo.GroupInfoBo;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.notice.SystemNotice;
import com.xsdkj.wechat.service.UserGroupService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.util.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/11/17 18:52
 */
@Slf4j
@Component
public class NettyServer implements Runnable {
    @Value("${netty.port}")
    private Integer port;
    @Resource(name = "bossGroup")
    private EventLoopGroup bossGroup;
    @Resource(name = "workerGroup")
    private EventLoopGroup workerGroup;
    @Resource
    private ServerBootstrap serverBootstrap;
    @Resource(name = "channelManager")
    private ChannelHandler channelManager;
    @Resource
    private SystemNotice imNotice;
    @Resource
    private UserGroupService groupService;
    @Resource
    private UserService userService;

    private ChannelFuture channelFuture;

    @Override
    public void run() {
        try {
            long begin = System.currentTimeMillis();
            // boss负责客户端的TCP连接请求，worker负责与客户端之间的读写操作
            serverBootstrap.group(bossGroup, workerGroup)
                    // 配置客户端channel类型
                    .channel(NioServerSocketChannel.class)
                    // 配置TCP参数，握手字符串长度设置
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // TCP_NODELAYT算法，尽可能发送大块数据，减少充斥的小块数据。
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 开启心跳包活机制，客户端，服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动。
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 配置固定长度接收缓存区分配器
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
                    // 绑定I/O事件的处理类
                    .childHandler(channelManager);
            channelFuture = serverBootstrap.bind(port).addListener(future -> {
                boolean success = future.isSuccess();
                if (success) {
                    ThreadUtil.getSingleton().submit(imNotice);
                    log.info("通知线程启动完毕!{}ms", DateUtil.spendMs(begin));
                    // 初始化群组
                    List<UserGroup> groups = groupService.listAllChatGroup();
                    if (groups.size() > 0) {
                        groups.forEach(group -> SessionUtil.GROUP_MAP.put(group.getId(), new GroupInfoBo(group, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE))));
                    }
                    log.info("群组初始化完毕!已启动{}个群组.{}ms", groups.size(), DateUtil.spendMs(begin));
                    groupService.updateRedisNoSayData();
                    log.info("禁言黑名单缓存完毕!{}ms", DateUtil.spendMs(begin));
                    log.info("NETTY服务器已在[{}]端口启动完毕!阻塞式等候客户端连接.耗时{}ms,", port, DateUtil.spendMs(begin));
                }
            }).sync();
        } catch (Exception e) {
            log.error(e.getMessage());
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }

    void close() {
        channelFuture.channel().close();
        Future<?> bossFuture = bossGroup.shutdownGracefully();
        Future<?> workerFuture = workerGroup.shutdownGracefully();
        try {
            bossFuture.await();
            workerFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
