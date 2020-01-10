package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.constant.RedisConstant;
import com.xsdkj.wechat.entity.wallet.SystemParameter;
import com.xsdkj.wechat.ex.ParseParamException;
import com.xsdkj.wechat.mapper.SystemParameterMapper;
import com.xsdkj.wechat.service.SystemParameterService;
import com.xsdkj.wechat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/5 20:00
 */
@Slf4j
@Service
public class SystemParameterServiceImpl implements SystemParameterService {
    @Resource
    private SystemParameterMapper systemParameterMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public SystemParameter getSystemParameter(boolean type) {
        if (type) {
            log.debug("获取系统参数...");
            long begin = System.currentTimeMillis();
            SystemParameter systemParameter;
            Object redisData = redisUtil.get(RedisConstant.REDIS_SYSTEM_PARAMETER);
            if (redisData != null) {
                try {
                    systemParameter = JSONObject.toJavaObject(JSONObject.parseObject(redisData.toString()), SystemParameter.class);
                } catch (Exception e) {
                    log.error("redis转换Java对象失败");
                    throw new ParseParamException();
                }
                log.debug("系统参数获取完成用时:{}", DateUtil.spendMs(begin));
                return systemParameter;
            }
            systemParameter = systemParameterMapper.selectByPrimaryKey(1);
            log.debug("redis缓存为空,从数据库查询参数并缓存至redis.系统参数为:{}", systemParameter);
            redisUtil.set(RedisConstant.REDIS_SYSTEM_PARAMETER, JSONObject.toJSONString(systemParameter));
            log.debug("系统参数获取完成用时:{}", DateUtil.spendMs(begin));
            return systemParameter;
        }
        return systemParameterMapper.selectByPrimaryKey(1);
    }
}
