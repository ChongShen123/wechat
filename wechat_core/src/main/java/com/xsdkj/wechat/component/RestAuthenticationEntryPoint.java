package com.xsdkj.wechat.component;

import cn.hutool.json.JSONUtil;
import com.xsdkj.wechat.common.JsonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当登录或者token失效访问接口时，自定义返回结果
 *
 * @author tiankong
 * @date 2019/11/17 15:55
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/admin")) {
            response.sendRedirect("/admin/login");
            return;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(401);
        response.getWriter().println(JSONUtil.parse(JsonResult.unauthorized()));
        response.getWriter().flush();
    }
}
