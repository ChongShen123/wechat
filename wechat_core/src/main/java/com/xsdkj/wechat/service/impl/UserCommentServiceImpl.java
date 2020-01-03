package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.dto.UserCommentDto;
import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.UserCommentService;
import com.xsdkj.wechat.util.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
public class UserCommentServiceImpl implements UserCommentService {
    @Resource
    private UserUtil userUtil;
    @Resource
    private RabbitTemplateService rabbitTemplateService;

    @Override
    public void save(UserCommentDto userCommentDto) {
        UserComment userComment = createUserComment(userCommentDto);
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_COMMENT, userComment));
    }

    private UserComment createUserComment(UserCommentDto userCommentDto) {
        UserComment userComment = new UserComment();
        userComment.setUid(userUtil.currentUser().getUser().getId());
        userComment.setNickname(userCommentDto.getNickname());
        userComment.setContent(userCommentDto.getContent());
        userComment.setCreateTimes(System.currentTimeMillis());
        userComment.setMoodId(userCommentDto.getMoodId());
        return userComment;
    }

    @Override
    public void delete(UserComment userComment) {

        if (userComment.getId() != null) {
            userComment.setUid(userUtil.currentUser().getUser().getId());
            rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_MOOD, userComment));
        }
    }
}