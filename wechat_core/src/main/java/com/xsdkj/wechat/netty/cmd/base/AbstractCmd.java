package com.xsdkj.wechat.netty.cmd.base;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

/**
 * 命令执行抽象类
 *
 * @author tiankong
 * @date 2019/11/18 11:53
 */
public abstract class AbstractCmd extends BaseHandler {

    /**
     * 命令类型
     */
    protected Integer cmd;

    /**
     * 执行命令
     *
     * @param param   参数
     * @param channel 用户channel
     */
    public abstract void execute(JSONObject param, Channel channel);


    /**
     * 获取命令
     * @return cmd
     */
    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

}
