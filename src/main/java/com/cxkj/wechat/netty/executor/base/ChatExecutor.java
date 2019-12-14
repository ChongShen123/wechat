package com.cxkj.wechat.netty.executor.base;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.service.RabbitTemplateService;
import com.cxkj.wechat.service.cache.GroupCache;
import com.cxkj.wechat.service.cache.UserCache;
import com.cxkj.wechat.util.JwtTokenUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 命令执行者抽象类
 *
 * @author tiankong
 * @date 2019/11/17 19:54
 */
@Service
public class ChatExecutor extends Executor {
    @Resource
    protected JwtTokenUtil jwtTokenUtil;
    @Resource
    protected UserCache userCache;
    @Resource
    protected GroupCache groupCache;
    @Resource
    protected RabbitTemplateService rabbitTemplateService;

    /**
     * 执行方法
     * 需要被子类重写
     *
     * @param param   参数
     * @param channel 用户channel
     */
    @Override
    public void execute(JSONObject param, Channel channel) {

    }
}
