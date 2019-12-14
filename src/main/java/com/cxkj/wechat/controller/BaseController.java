package com.cxkj.wechat.controller;

import com.cxkj.wechat.service.ex.ServiceException;
import com.cxkj.wechat.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author tiankong
 * @date 2019/12/11 14:57
 */
public abstract class BaseController {
//    @ExceptionHandler({ServiceException.class, Exception.class})
//    public JsonResult handleException(Throwable e) {
//        if (e instanceof ServiceException) {
//            return JsonResult.failed(((ServiceException) e).getCode());
//        }
//        e.printStackTrace();
//        return JsonResult.failed();
//    }
}
