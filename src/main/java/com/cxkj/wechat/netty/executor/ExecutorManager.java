package com.cxkj.wechat.netty.executor;


import com.cxkj.wechat.netty.executor.base.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行者管理类
 *
 * @author tiankong
 * @date 2019/11/17 19:58
 */
@Component
@Slf4j
public class ExecutorManager implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<Integer, Executor> commandTypeMap = new HashMap<>();

    public Executor getCommand(Integer type) {
        return commandTypeMap.get(type);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 获取所有带有 ExecutorAnno 注解的bean.
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(ExecutorAnno.class);
        // 将这些bean添加到 commandTypeMap中
        beans.forEach((name, bean) -> {
            ExecutorAnno annotation = bean.getClass().getAnnotation(ExecutorAnno.class);
            try {
                Method method = bean.getClass().getMethod("setCommand", Integer.class);
                if (method != null) {
                    method.invoke(bean, annotation.command());
                }
            } catch (Exception e) {
                log.error(bean.getClass().getName() + "初始化命令失败");
                e.printStackTrace();
            }
            commandTypeMap.put(annotation.command(), (Executor) bean);
        });
    }
}
