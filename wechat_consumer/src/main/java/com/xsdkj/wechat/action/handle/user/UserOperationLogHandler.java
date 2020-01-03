package com.xsdkj.wechat.action.handle.user;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 13:01
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_USER_OPERATION_LOG)
public class UserOperationLogHandler implements MsgHandler {
    @Resource
    private UserService userService;

    @Override
    public void execute(MsgBox box) {
        UserOperationLog userOperationLog = (UserOperationLog) box.getData();
        userService.saveUserOperationLog(userOperationLog);
    }
}
