package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.ex.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * @author tiankong
 * @date 2019/12/11 18:28
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public JsonResult methodArgumentExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getMessage();
        message = message.substring(message.lastIndexOf("[") + 1, message.lastIndexOf("]") - 1);
        log.error("参数异常>>>>{}", message);
        return JsonResult.failed(message);
    }

    @ExceptionHandler(value = ServiceException.class)
    public JsonResult serviceExceptionHandler(ServiceException e) {
        log.error("业务异常>>>>{}",e.getCode());
        return JsonResult.failed(e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public JsonResult exceptionHandler(Exception e) {
        log.error("未知异常>>>>{}", e);
        return JsonResult.failed();
    }
}
