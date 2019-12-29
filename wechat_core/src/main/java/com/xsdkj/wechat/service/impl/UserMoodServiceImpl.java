package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.entity.chat.UserMood;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.UserMoodService;
import com.xsdkj.wechat.util.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public abstract class UserMoodServiceImpl implements UserMoodService {
    @Resource
    private UserUtil userUtil;
    @Resource
    private RabbitTemplateService rabbitTemplateService;
    @Override
    public void save(MoodParamDto moodDto) {
        UserMood userMood  = createNewUserMood(moodDto);
        rabbitTemplateService.addExchange(SystemConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_MOOD,userMood));
    }

    @Override
    public void delete(UserMood userMood) {
        if(userMood.getId()!=null){
            userMood.setUid(userUtil.currentUser().getUser().getId());
            rabbitTemplateService.addExchange(SystemConstant.FANOUT_SERVICE_NAME,RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_MOOD,userMood));
        }


    }

    private UserMood createNewUserMood(MoodParamDto moodDto) {
        UserMood userMood = new UserMood();
        userMood.setContent(moodDto.getContent());
        userMood.setFile(moodDto.getFile());
        userMood.setFileType(moodDto.getFileType());
        userMood.setCreateTimes(System.currentTimeMillis());
        userMood.setUid(userUtil.currentUser().getUser().getId());
        return userMood;
    }
}
