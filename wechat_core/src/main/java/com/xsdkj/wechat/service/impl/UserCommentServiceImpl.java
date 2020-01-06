package com.xsdkj.wechat.service.impl;






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

        userCommentMapper.insert(userComment);
        /*rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_COMMENT, userComment));*/



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

    if(userComment.getId()!=null){
        userComment.setUid(userUtil.currentUser().getUser().getId());
        userCommentMapper.deleteByPrimaryKey(userComment.getId());
       /* rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_COMMENT,userComment));*/
    }




        if (userComment.getId() != null) {
            userComment.setUid(userUtil.currentUser().getUser().getId());

        }

    }
}
