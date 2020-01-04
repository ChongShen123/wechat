package com.xsdkj.wechat.service.impl;

<<<<<<< HEAD


=======
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
>>>>>>> 27edae3208a992a05a995390701c4070e0a6af6c
import com.xsdkj.wechat.dto.UserCommentDto;
import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.mapper.UserCommentMapper;
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
    UserCommentMapper userCommentMapper;
    @Override
    public void save(UserCommentDto userCommentDto) {
        UserComment userComment = createUserComment(userCommentDto);
<<<<<<< HEAD
        userCommentMapper.insert(userComment);
        /*rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_COMMENT, userComment));*/
=======
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_COMMENT, userComment));
>>>>>>> 27edae3208a992a05a995390701c4070e0a6af6c
    }

    private UserComment createUserComment(UserCommentDto userCommentDto) {
        UserComment userComment = new UserComment();
        userComment.setUid(userUtil.currentUser().getUser().getId());
        userComment.setNickname(userCommentDto.getNickname());
        userComment.setContent(userCommentDto.getContent());
        userComment.setCreateTimes(System.currentTimeMillis());
        if (userCommentDto.getMoodId()!=null){
            userComment.setMoodId(userCommentDto.getMoodId());
        }
        return userComment;
    }

    @Override
    public void delete(UserComment userComment) {
<<<<<<< HEAD
    if(userComment.getId()!=null){
        userComment.setUid(userUtil.currentUser().getUser().getId());
        userCommentMapper.deleteByPrimaryKey(userComment.getId());
       /* rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_COMMENT,userComment));*/
    }


=======

        if (userComment.getId() != null) {
            userComment.setUid(userUtil.currentUser().getUser().getId());
            rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_MOOD, userComment));
        }
>>>>>>> 27edae3208a992a05a995390701c4070e0a6af6c
    }
}
