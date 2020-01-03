package com.xsdkj.wechat.action.handle;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户注册消息储存
 * @author tiankong
 * @date 2020/1/2 10:28
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_USER_REGISTER)
public class UserRegisterHandler implements MsgHandler {
    @Resource
    private UserService userService;

    @Override
    public void execute(RabbitMessageBoxBo box) {
        User user = (User) box.getData();
        userService.save(user);
    }
}
