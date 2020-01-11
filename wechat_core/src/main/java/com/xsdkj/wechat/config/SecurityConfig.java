package com.xsdkj.wechat.config;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.component.JwtAuthenticationTokenFilter;
import com.xsdkj.wechat.component.RestAuthenticationEntryPoint;
import com.xsdkj.wechat.component.RestfulAccessDeniedHandler;
import com.xsdkj.wechat.constant.RedisConstant;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/11/17 16:17
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/",
                        "*.html",
                        "/favicon.ico",
                        "**/*.html",
                        "**/*.js",
                        "**/*.css",
                        "swagger-resources/**",
                        "v2/api-docs/**")
                .permitAll()
                .antMatchers("/user/login",
                        "/user/register",
                        "/file/upload/img",
                        "/admin/login",
                        "/layuiadmin/**",
                        "/common/**")
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
//                .antMatchers("**")
//                .permitAll()
                .anyRequest()
                .authenticated();
        //禁用缓存
        http.headers().cacheControl();
        // 添加Jwt filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        //Spring Security禁用X-Frame-Options 不加此配置.在网页内部 iframe 来能加载其他网页
        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return username -> {
            long begin = System.currentTimeMillis();
            log.debug(LogUtil.INTERVAL);
            try {
                Object userData = redisUtil.get(RedisConstant.REDIS_USERNAME + username);
                User user;
                if (userData != null) {
                    user = JSONObject.toJavaObject(JSONObject.parseObject(userData.toString()), User.class);
                } else {
                    user = userService.getByUsername(username);
                    if (user == null) {
                        throw new UsernameNotFoundException("用户名或密码错误");
                    }
                    redisUtil.set(RedisConstant.REDIS_USERNAME + user.getUsername(), JSONObject.toJSONString(user), RedisConstant.REDIS_USER_TIMEOUT);
                }
                log.debug("当前用户{} ", user);
                return new UserDetailsBo(user);
            } finally {
                log.debug("用时 {}ms", DateUtil.spendMs(begin));
            }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
