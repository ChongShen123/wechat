package com.xsdkj.wechat.netty.cmd;


import com.xsdkj.wechat.netty.cmd.base.AbstractCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令执行者管理类
 *
 * @author tiankong
 * @date 2019/11/17 19:58
 */
@Component
@Slf4j
public class CmdManager implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<Integer, AbstractCmd> commandTypeMap = new HashMap<>();
//    private static Map<Integer, CommandBo> methodMap = new HashMap<>();

//    public CommandBo getCommandBo(Integer byteType) {
//        return methodMap.get(byteType);
//    }

    public AbstractCmd getCommand(Integer type) {
        return commandTypeMap.get(type);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 获取所有带有 CmdAnno 注解的bean.
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(CmdAnno.class);
        // 将这些bean添加到 commandTypeMap中
        beans.forEach((name, bean) -> {
            // 这样就可以将扫描方法上的注解了。
//            Method[] methods = bean.getClass().getMethods();
//            for (Method method : methods) {
//                CmdAnno annotation = method.getAnnotation(CmdAnno.class);
//                CommandBo commandBo = new CommandBo();
//                commandBo.setCmd(annotation.cmd());
//                commandBo.setMethod(method);
//                commandBo.setObject(bean);
//                methodMap.put(annotation.cmd(), commandBo);
//            }
            CmdAnno annotation = bean.getClass().getAnnotation(CmdAnno.class);
            try {
                // 为执行者类设置对应的 cmd
                Method method = bean.getClass().getMethod("setCmd", Integer.class);
                if (method != null) {
                    method.invoke(bean, annotation.cmd());
                }
            } catch (Exception e) {
                log.error(bean.getClass().getName() + "初始化命令失败");
                e.printStackTrace();
            }
            commandTypeMap.put(annotation.cmd(), (AbstractCmd) bean);
        });
    }
}
