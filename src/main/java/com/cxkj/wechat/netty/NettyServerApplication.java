package com.cxkj.wechat.netty;

import com.cxkj.wechat.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * @author tiankong
 * @date 2019/11/17 18:49
 */
@Component
@Slf4j
public class NettyServerApplication {
    @Resource
    private NettyServer nettyServer;
    private ExecutorService service;

    @PostConstruct
    public void init() {
        service = ThreadUtil.getSingleton();
        service.execute(nettyServer);
    }

    @PreDestroy
    public void close() {
        nettyServer.close();
        service.shutdown();
    }
}
