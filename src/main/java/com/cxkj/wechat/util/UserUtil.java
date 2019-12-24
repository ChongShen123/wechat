package com.cxkj.wechat.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.CurrentUserDetailsBo;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tiankong
 * @date 2019/12/11 13:05
 */
@Component
public class UserUtil {
    @Resource
    private UserService userService;

    public CurrentUserDetailsBo currentUser() {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        CurrentUserDetailsBo currentUserDetailsBo = new CurrentUserDetailsBo(user);
        currentUserDetailsBo.setPermissionBos(userService.getUserPermission(user.getId()));
        return currentUserDetailsBo;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        String Trade_No = "Q2019C23817494791958";
        Integer price = 298;
        String ChannelType = "ali_h5";
        String NotifyUrl = "http://www.baidu.com";
        String LoginName = "cishi";
        String data = LoginName + "5BA9307EF0E718B07CE52369FA2CA26A" + Trade_No + price;
        System.out.println(data);
        String sign = SecureUtil.md5(data).toUpperCase();
        map.put("Trade_No", Trade_No);
        map.put("Price", price);
        map.put("ChannelType", ChannelType);
        map.put("NotifyUrl", NotifyUrl);
        map.put("LoginName", LoginName);
        map.put("Sign", sign);
        System.out.println(sign);
        String body = JSONObject.toJSONString(map);
        System.out.println(body);
        String post = HttpUtil.post("http://1.yesmaile.com/admin/httphand/CreateOrder.ashx", body);
        System.out.println(post);
    }
}
