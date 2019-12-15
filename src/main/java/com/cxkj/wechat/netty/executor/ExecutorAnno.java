package com.cxkj.wechat.netty.executor;

import java.lang.annotation.*;

/**
 * 命令执行者注解
 * 带有该注解的类都将会标记为命令执行类
 *
 * @author tiankong
 * @date 2019/12/13 17:47
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutorAnno {
    int command();
}
