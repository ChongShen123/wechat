package com.cxkj.wechat;

import com.cxkj.wechat.component.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMailApplicationTest {
    @Resource
    MailService mailService;
    @Test
    public void sendSimpleMail(){
        /**
         * 发件人，收件人，抄送，邮件主题，邮件内容
         */
        mailService.sendSimpleMail("404990596@qq.com","1851983671@qq.com",
                "789456123@qq.com","主题","内容");
    }
}
