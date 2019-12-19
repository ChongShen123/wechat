package com.cxkj.wechat;

import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@SpringBootTest
public class UpdateUser {
    @Resource
    UserMapper userMapper;
    @Test
    public void test(){
        User user=new User();
        user.setId(7);
        user.setIcon("455454");
        userMapper.updateByPrimaryKeySelective(user);

    }


}
