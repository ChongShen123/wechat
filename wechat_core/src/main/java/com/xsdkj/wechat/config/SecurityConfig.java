package com.xsdkj.wechat.config;


import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.component.JwtAuthenticationTokenFilter;
import com.xsdkj.wechat.component.RestAuthenticationEntryPoint;
import com.xsdkj.wechat.component.RestfulAccessDeniedHandler;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.service.UserService;
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
        log.debug("SpringSecurity 框架现在检查用户信息");
        return username -> {
            User user = userService.getByUsername(username);
            if (user != null) {
                return new UserDetailsBo(user);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
