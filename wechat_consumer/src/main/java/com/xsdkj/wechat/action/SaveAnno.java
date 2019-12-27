package com.xsdkj.wechat.action;

import java.lang.annotation.*;

/**
 * 消息存储注解
 * 添加此注解的类被标记为一个具体消息执行者.
 * @author tiankong
 * @date 2019/12/27 10:46
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SaveAnno {
    /**
     * 信息类型
     * @return int
     */
    int type() default -1;
}
