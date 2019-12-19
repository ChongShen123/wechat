package com.cxkj.wechat.bo;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author tiankong
 * @date 2019/12/18 17:51
 */
@Data
public class CommandBo {
    /**
     * 命令
     */
    int cmd;
    /**
     * 对象的引用
     */
    Object object;
    /**
     * 方法
     */
    Method method;
}
