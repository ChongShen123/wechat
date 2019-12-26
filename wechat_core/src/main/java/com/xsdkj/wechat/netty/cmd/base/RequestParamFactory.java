package com.xsdkj.wechat.netty.cmd.base;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RequestParamBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.netty.ex.ParseParamException;
import com.xsdkj.wechat.netty.ex.ValidateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xsdkj.wechat.common.SystemConstant.KEY_CONTENT;
import static com.xsdkj.wechat.common.SystemConstant.KEY_GROUP_ID;


/**
 * WebSocket业务参数解析工厂
 *
 * @author tiankong
 * @date 2019/12/15 15:38
 */
//@Component
@Deprecated
public class RequestParamFactory {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 解析请求参数
     *
     * @param command 命令类型
     * @param param   请求参数JSON
     * @return 参数对象
     */
    RequestParamBo getParam(Integer command, JSONObject param) {
        RequestParamBo requestParam = new RequestParamBo();
        switch (command) {
            case Cmd.REGISTER:
                String token = param.getString(KEY_CONTENT);
                if (ObjectUtil.isEmpty(token)) {
                    throw new ValidateException();
                }
                requestParam.setContent(token);
                break;
            case Cmd.SINGLE_CHAT_CANCEL:
                String id = param.getString(SystemConstant.KEY_ID);
                Integer uid = param.getInteger(SystemConstant.KEY_TO_USER_ID);
                if (id == null || uid == null) {
                    throw new ValidateException();
                }
                requestParam.setId(id);
                requestParam.setUserId(uid);
                break;
            case Cmd.SINGLE_CHAT:
                Integer toUserId = param.getInteger(SystemConstant.KEY_TO_USER_ID);
                String content = param.getString(SystemConstant.KEY_CONTENT);
                Byte type = param.getByte(SystemConstant.KEY_TYPE);
                if (ObjectUtil.isEmpty(toUserId) || ObjectUtil.isEmpty(content) || ObjectUtil.isEmpty(type)) {
                    throw new ValidateException();
                }
                requestParam.setToUserId(toUserId);
                requestParam.setContent(content);
                requestParam.setType(type);
                break;
            case Cmd.ADD_FRIEND:
                String username = param.getString(SystemConstant.KEY_USERNAME);
                content = param.getString(SystemConstant.KEY_CONTENT);
                if (StrUtil.isBlank(username) || StrUtil.isBlank(content)) {
                    throw new ValidateException();
                }
                requestParam.setUsername(username);
                requestParam.setMessage(content);
                break;
            case Cmd.FRIEND_AGREE:
                id = param.getString(SystemConstant.KEY_ID);
                Byte state = param.getByte(SystemConstant.KEY_STATE);
                if (!(state == SystemConstant.AGREE || state == SystemConstant.REFUSE)) {
                    throw new RuntimeException();
                }
                requestParam.setId(id);
                requestParam.setState(state);
                break;
            case Cmd.GROUP_INFO:
            case Cmd.GROUP_BASE_INFO:
            case Cmd.LIST_GROUP_MEMBERS:
                Integer groupId = param.getInteger(KEY_GROUP_ID);
                if (ObjectUtil.isEmpty(groupId)) {
                    throw new ValidateException();
                }
                requestParam.setGroupId(groupId);
                break;
            case Cmd.CREATE_GROUP:
            case Cmd.JOIN_GROUP:
            case Cmd.REMOVE_CHAT_GROUP:
                try {
                    Set<Integer> ids;
                    ids = Arrays.stream(StrUtil.splitToInt(param.getString(SystemConstant.KEY_IDS), ",")).boxed().collect(Collectors.toSet());
                    groupId = param.getInteger(KEY_GROUP_ID);
                    if (ObjectUtil.isNotEmpty(groupId)) {
                        requestParam.setGroupId(groupId);
                    }
                    requestParam.setIds(ids);
                } catch (Exception e) {
                    throw new ValidateException();
                }
                break;
            case Cmd.GROUP_CHAT:
                try {
                    groupId = param.getInteger(KEY_GROUP_ID);
                    content = param.getString(SystemConstant.KEY_CONTENT);
                    type = param.getByte(SystemConstant.KEY_TYPE);
                    requestParam.setGroupId(groupId);
                    requestParam.setContent(content);
                    requestParam.setType(type);
                } catch (Exception e) {
                    throw new ValidateException();
                }
                break;
            case Cmd.LIST_USER_GROUP:
                break;
            default:
                throw new ParseParamException();
        }
        return requestParam;
    }
}
