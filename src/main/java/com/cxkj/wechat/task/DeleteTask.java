package com.cxkj.wechat.task;


import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.service.SingleChatService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


import javax.annotation.Resource;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DeleteTask {
    private static  final Logger logger= LoggerFactory.getLogger(DeleteTask.class);
    @Resource
    SingleChatService singleChatService;

    public void deleteCreateTimes(SingleChat singleChat){

           singleChatService.deleteTask(singleChat);

    }


}
