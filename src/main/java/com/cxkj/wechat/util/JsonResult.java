package com.cxkj.wechat.util;


import com.cxkj.wechat.constant.IErrorCode;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.vo.ChatResponse;
import lombok.Data;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/11/17 15:27
 */
@Data
public class JsonResult<T> {
    private Integer code;
    private String message;
    private Byte type;
    private T data;

    public JsonResult() {
        super();
    }

    public JsonResult(Integer state, String message, byte type, T data) {
        this.code = state;
        this.message = message;
        this.type = type;
        this.data = data;
    }

    public JsonResult(Integer state) {
        super();
        this.code = state;
    }

    public JsonResult(Throwable throwable) {
        super();
        this.message = throwable.getMessage();
    }

    public JsonResult(Integer state, T data) {
        super();
        this.code = state;
        this.data = data;
    }

    private JsonResult(Integer state, String message) {
        this.code = state;
        this.message = message;
    }

    private JsonResult(IErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();

    }

    private JsonResult(IErrorCode success, T loginInfo) {
        this.code = success.getCode();
        this.message = success.getMessage();
        this.data = loginInfo;
    }


    private JsonResult(IErrorCode success, byte type) {
        this.code = success.getCode();
        this.message = success.getMessage();
        this.type = type;
    }

    /**
     * 未授权
     */
    public static JsonResult forbidden() {
        return new JsonResult(ResultCodeEnum.FORBIDDEN);
    }

    /**
     * 未登录
     */
    public static JsonResult unauthorized() {
        return new JsonResult(ResultCodeEnum.UNAUTHORIZED);
    }

    public static JsonResult success(Object loginInfo) {
        JsonResult<Object> jsonResult = new JsonResult<>();
        jsonResult.setCode(ResultCodeEnum.SUCCESS.getCode());
        jsonResult.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        jsonResult.setData(loginInfo);
        return jsonResult;
    }

    public static <T> JsonResult success(List<T> loginInfo, byte command) {
        return new JsonResult(ResultCodeEnum.SUCCESS, loginInfo);
    }


    public static JsonResult success() {
        return new JsonResult(ResultCodeEnum.SUCCESS);
    }

    public static JsonResult success(String msg) {
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static JsonResult success(byte type) {
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), type, null);
    }

    public static JsonResult success(String msg, byte type) {
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), msg, type, null);
    }


    /**
     * 业务异常
     *
     * @param errorCode 异常类型
     * @return JsonResult
     */
    public static JsonResult<Void> failed(IErrorCode errorCode) {
        return new JsonResult<>(errorCode);
    }

    public static JsonResult<Void> failed(IErrorCode errorCode, byte type) {
        return new JsonResult<>(errorCode, type);
    }

    public static JsonResult failed(String msg, byte type) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(ResultCodeEnum.FAILED.getCode());
        jsonResult.setMessage(msg);
        jsonResult.setType(type);
        return jsonResult;
    }

    public static JsonResult<Void> failed(byte type) {
        return new JsonResult<>(ResultCodeEnum.FAILED, type);
    }

    /**
     * 操作失败
     *
     * @return JsonResult
     */
    public static JsonResult<Void> failed() {
        return new JsonResult<>(ResultCodeEnum.FAILED);
    }

    public static JsonResult failed(String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(ResultCodeEnum.FAILED.getCode());
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public static JsonResult success(FriendApplication friendApplication, byte command) {
        JsonResult<FriendApplication> result = new JsonResult<>();
        result.setData(friendApplication);
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setType(command);
        return result;
    }


    public static JsonResult success(SingleChat chat, byte command) {
        JsonResult<SingleChat> result = new JsonResult<>();
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setData(chat);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setType(command);
        return result;
    }

    public static JsonResult success(ChatResponse response, byte command) {
        JsonResult<ChatResponse> result = new JsonResult<>();
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setData(response);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setType(command);
        return result;
    }
}
