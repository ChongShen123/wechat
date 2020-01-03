package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.service.UserCommentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author panda
 */
@Component
@SaveAnno(type = SystemConstant.BOX_TYPE_COMMENT)
public class UserCommentHandler implements MsgHandler {
    @Resource
    UserCommentService userCommentService;
    @Override
    public void execute(RabbitMessageBoxBo box){
        UserComment userComment= (UserComment) box.getData();
        userCommentService.save(userComment);
/*        userCommentService.delete(userComment);*/
    }
}
