package com.zjd.mall.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zjd
 * @Description Myabtis 配置类
 * @date 2020/4/16 15:23
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.zjd.mall.mapper"})
public class MyBatisConfig
{

}
