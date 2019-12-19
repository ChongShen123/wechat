package com.cxkj.wechat.task;


import com.cxkj.wechat.service.SingleChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component     // 1.主要用于标记配置类，兼备Component的效果。
public class DeleteWeekTask {
   @Autowired
    SingleChatService singleChatService;
   //0 */1 * * * ?        0 15 10 15 * ?
    @Scheduled(cron = "0 */1 * * * ?" )
    public void deleteCreateTimes(){
          singleChatService.deleteTask();
          singleChatService.deleteImage();
    }
}
