package com.xsdkj.wechat.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tiankong
 * @date 2019/12/11 13:05
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.xsdkj.wechat.mapper")
public class MybatisConfig {

}
