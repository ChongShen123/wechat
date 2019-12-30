package com.xsdkj.wechat.common;

import lombok.Data;

import java.util.Map;

/**
 * @author tiankong
 * @date 2019/11/17 15:27
 */
@Data
public class JsonResult<T> {
    private Integer code;
    private String message;
    private Integer type;
    private T data;

    public static JsonResult forbidden() {
        JsonResult result = new JsonResult<>();
        result.setCode(ResultCodeEnum.FORBIDDEN.getCode());
        result.setMessage(ResultCodeEnum.FORBIDDEN.getMessage());
        return result;
    }

    public static JsonResult unauthorized() {
        JsonResult result = new JsonResult<>();
        result.setCode(ResultCodeEnum.UNAUTHORIZED.getCode());
        result.setMessage(ResultCodeEnum.UNAUTHORIZED.getMessage());
        return result;
    }

    public static JsonResult success() {
        JsonResult result = new JsonResult<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return result;
    }

    public static JsonResult success(String msg) {
        JsonResult<Object> result = new JsonResult<>();
        result.setMessage(msg);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    public static JsonResult success(Integer type) {
        JsonResult<Object> result = new JsonResult<>();
        result.setType(type);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return result;
    }

    public static JsonResult success(Object data) {
        JsonResult<Object> jsonResult = new JsonResult<>();
        jsonResult.setCode(ResultCodeEnum.SUCCESS.getCode());
        jsonResult.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult success(String msg, Integer type) {
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(msg);
        result.setType(type);
        return result;
    }

    public static JsonResult success(Object response, Integer command) {
        JsonResult<Object> result = new JsonResult<>();
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setData(response);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setType(command);
        return result;
    }

    public static JsonResult failed() {
        JsonResult result = new JsonResult<>();
        result.setCode(ResultCodeEnum.FAILED.getCode());
        result.setMessage(ResultCodeEnum.FAILED.getMessage());
        return result;
    }

    public static JsonResult failed(Map map, Integer cmd) {
        JsonResult<Map> result = new JsonResult<>();
        result.setCode(ResultCodeEnum.FAILED.getCode());
        result.setData(map);
        result.setType(cmd);
        return result;
    }

    public static JsonResult failed(long times, Integer type) {
        JsonResult<Long> result = new JsonResult<>();
        result.setCode(ResultCodeEnum.FAILED.getCode());
        result.setData(times);
        result.setType(type);
        return result;
    }

    public static JsonResult failed(String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(ResultCodeEnum.FAILED.getCode());
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public static JsonResult failed(IErrorCode errorCode) {
        JsonResult result = new JsonResult<>();
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        return result;
    }

    public static JsonResult failed(String msg, Integer type) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(ResultCodeEnum.FAILED.getCode());
        jsonResult.setMessage(msg);
        jsonResult.setType(type);
        return jsonResult;
    }

    public static JsonResult<Void> failed(Integer type) {
        JsonResult<Void> result = new JsonResult<>();
        result.setType(type);
        result.setMessage(ResultCodeEnum.FAILED.getMessage());
        result.setCode(ResultCodeEnum.FAILED.getCode());
        return result;
    }

    public static JsonResult<Void> failed(IErrorCode errorCode, Integer type) {
        JsonResult<Void> result = new JsonResult<>();
        result.setType(type);
        result.setMessage(errorCode.getMessage());
        result.setCode(errorCode.getCode());
        return result;
    }
}
