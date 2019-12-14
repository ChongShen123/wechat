package com.cxkj.wechat.controller;

import com.cxkj.wechat.service.ex.ServiceException;
import com.cxkj.wechat.util.JsonResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author tiankong
 * @date 2019/12/11 18:28
 */
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public JsonResult exceptionHandler(Throwable e) {
        if (e instanceof ServiceException) {
            return JsonResult.failed(((ServiceException) e).getCode());
        } else if (e instanceof MethodArgumentNotValidException) {
            String message = e.getMessage();
            message = message.substring(message.lastIndexOf("[")+1, message.lastIndexOf("]")-1);
            return JsonResult.failed(message);
        }
        e.printStackTrace();
        return JsonResult.failed();
    }
}
