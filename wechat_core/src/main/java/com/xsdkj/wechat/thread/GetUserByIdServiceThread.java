package com.xsdkj.wechat.thread;

import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.service.UserService;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 查询用户服务线程
 *
 * @author tiankong
 * @date 2020/1/11 11:34
 */
public class GetUserByIdServiceThread implements Callable<User> {

    private UserService userService;
    private CountDownLatch countDownLatch;
    private Integer uid;

    public GetUserByIdServiceThread(UserService userService, CountDownLatch countDownLatch, Integer uid) {
        this.userService = userService;
        this.countDownLatch = countDownLatch;
        this.uid = uid;
    }

    public GetUserByIdServiceThread(UserService userService, Integer uid) {
        this.userService = userService;
        this.uid = uid;
    }

    @Override
    public User call() throws Exception {
        try {
            return userService.getRedisData(uid);
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }
}
