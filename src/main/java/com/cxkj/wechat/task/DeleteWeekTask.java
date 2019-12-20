package com.cxkj.wechat.task;


import com.cxkj.wechat.service.GroupChatService;
import com.cxkj.wechat.service.SingleChatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component     // 1.主要用于标记配置类，兼备Component的效果。
public class DeleteWeekTask {
   @Resource
   SingleChatService singleChatService;
   @Resource
    GroupChatService groupChatService;

   //0 */1 * * * ?   -1分钟定时器     0 0/5 * * * ? 5分钟
    @Scheduled(cron = "0 0/5 * * * ?" )
    public void deleteCreateTimes(){
          singleChatService.deleteTask();
          singleChatService.deleteImage();
          groupChatService.deleteGroup();
          groupChatService.deleteGroupImage();
    }
}
