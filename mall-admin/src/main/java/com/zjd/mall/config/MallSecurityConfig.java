package com.zjd.mall.config;


import com.zjd.mall.security.config.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author zjd
 * @Description mall-security模块相关配置
 * @date 2020/4/16 15:25
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MallSecurityConfig extends SecurityConfig {

}
