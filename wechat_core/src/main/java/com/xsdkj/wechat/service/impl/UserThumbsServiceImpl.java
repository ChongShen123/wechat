package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.dto.UserThumbsDto;
import com.xsdkj.wechat.entity.mood.UserThumbs;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.UserThumbsService;
import com.xsdkj.wechat.util.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class UserThumbsServiceImpl implements UserThumbsService {
    @Resource
    private UserUtil userUtil;
    @Resource
    private RabbitTemplateService rabbitTemplateService;

    /**
     * 保存用户点赞
     * @param userThumbsDto
     */
    @Override
    public void save(UserThumbsDto userThumbsDto) {
        UserThumbs userThumbs=saveThums(userThumbsDto);
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_THUMS,userThumbs));
    }
    private UserThumbs saveThums(UserThumbsDto userThumbsDto) {
        UserThumbs userThumbs=new UserThumbs();
        userThumbs.setUid(userUtil.currentUser().getUser().getId());
        userThumbs.setNickname(userThumbsDto.getNickname());
        userThumbs.setMoodId(userThumbsDto.getMoodId());
        return  userThumbs;
    }
    /**
     * 删除用户的点赞
     * @param userThumbs
     */
    @Override
    public void delete(UserThumbs userThumbs) {
        if(userThumbs.getId()!=null){
            userThumbs.setUid(userUtil.currentUser().getUser().getId());
            rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME,RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_MOOD,userThumbs));
        }
    }

}
