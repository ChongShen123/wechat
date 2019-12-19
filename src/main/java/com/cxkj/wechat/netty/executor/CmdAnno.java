package com.cxkj.wechat.netty.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 命令执行者注解
 * 带有该注解的类都将会标记为命令执行类
 *
 * @author tiankong
 * @date 2019/12/18 19:04
 */
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CmdAnno {
    int value() default -1;
}
