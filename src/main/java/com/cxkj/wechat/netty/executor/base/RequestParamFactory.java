package com.cxkj.wechat.netty.executor.base;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.netty.ex.ParseParamException;
import com.cxkj.wechat.netty.ex.ValidateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.cxkj.wechat.constant.SystemConstant.KEY_TOKEN;

import com.cxkj.wechat.constant.Command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tiankong
 * @date 2019/12/15 15:38
 */
@Component
public class RequestParamFactory {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    RequestParamBo getParam(Integer command, JSONObject param) {
        RequestParamBo requestParamBo = new RequestParamBo();
        switch (command) {
            case Command.REGISTER:
                String token = param.getString(KEY_TOKEN);
                if (ObjectUtil.isEmpty(token)) {
                    throw new ValidateException();
                }
                requestParamBo.setToken(token);
                break;
            case Command.SINGLE_CHAT_CANCEL:
                String id = param.getString(SystemConstant.KEY_ID);
                Integer uid = param.getInteger(SystemConstant.KEY_TO_USER_ID);
                if (id == null || uid == null) {
                    throw new ValidateException();
                }
                requestParamBo.setId(id);
                requestParamBo.setUserId(uid);
                break;
            case Command.SINGLE_CHAT:
                Integer toUserId = param.getInteger(SystemConstant.KEY_TO_USER_ID);
                String content = param.getString(SystemConstant.KEY_CONTENT);
                Integer type = param.getInteger(SystemConstant.KEY_TYPE);
                if (ObjectUtil.isEmpty(toUserId) || ObjectUtil.isEmpty(content) || ObjectUtil.isEmpty(type)) {
                    throw new ValidateException();
                }
                requestParamBo.setToUserId(toUserId);
                requestParamBo.setContent(content);
                requestParamBo.setType(type);
                break;
            case Command.ADD_FRIEND:
                String username = param.getString(SystemConstant.KEY_USERNAME);
                String message = param.getString(SystemConstant.KEY_MESSAGE);
                if (StrUtil.isBlank(username) || StrUtil.isBlank(message)) {
                    throw new ValidateException();
                }
                requestParamBo.setUsername(username);
                requestParamBo.setMessage(message);
                break;
            case Command.FRIEND_AGREE:
                id = param.getString(SystemConstant.KEY_ID);
                Byte state = param.getByte(SystemConstant.KEY_STATE);
                if (!(state == SystemConstant.AGREE || state == SystemConstant.REFUSE)) {
                    throw new RuntimeException();
                }
                requestParamBo.setId(id);
                requestParamBo.setState(state);
                break;
            case Command.GROUP_INFO:
            case Command.GROUP_BASE_INFO:
                Integer groupId = param.getInteger(SystemConstant.KEY_GROUP_ID);
                if (ObjectUtil.isEmpty(groupId)) {
                    throw new ValidateException();
                }
                requestParamBo.setGroupId(groupId);
                break;
            case Command.CREATE_GROUP:
                List<Integer> ids;
                try {
                    ids = Arrays.stream(StrUtil.splitToInt(param.getString(SystemConstant.KEY_IDS), ",")).boxed().collect(Collectors.toList());
                } catch (Exception e) {
                    throw new ValidateException();
                }
                requestParamBo.setIds(ids);
                break;
            default:
                throw new ParseParamException();
        }
        return requestParamBo;
    }
}
