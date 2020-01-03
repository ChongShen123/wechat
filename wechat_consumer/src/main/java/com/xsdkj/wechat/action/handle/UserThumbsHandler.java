package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.mood.UserThumbs;
import com.xsdkj.wechat.service.UserThumbsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author  p
 */
@Component
@SaveAnno(type = SystemConstant.BOX_TYPE_THUMS)
public class UserThumbsHandler implements MsgHandler {
    @Resource
    UserThumbsService userThumbsService;
    @Override
    public void execute(MsgBox box) {
       UserThumbs thumbs = (UserThumbs) box.getData();
        userThumbsService.save(thumbs);
    }
}