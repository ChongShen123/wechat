package com.xsdkj.wechat.action.handle;


import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.mood.UserMood;
import com.xsdkj.wechat.service.UserMoodService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Component
@SaveAnno(type = SystemConstant.BOX_TYPE_MOOD)
public class MoodHandler implements MsgHandler {
    @Resource
    private UserMoodService userMoodService;
    @Override
    public void execute(RabbitMessageBoxBo box) {
        System.out.println(box);
        UserMood mood = (UserMood) box.getData();
        userMoodService.save(mood);
//        userMoodService.delete(mood);
    }

}
