package com.cxkj.wechat.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tiankong
 * @date 2019/12/11 13:05
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.cxkj.wechat.mapper")
public class MybatisConfig {

}
