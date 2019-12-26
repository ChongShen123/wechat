package com.xsdkj.wechat.component;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MailService {
    @Resource
    JavaMailSender javaMailSender;
    public void sendSimpleMail(String from, String to,String cc,String subject,String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setCc(cc);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
    }
}
