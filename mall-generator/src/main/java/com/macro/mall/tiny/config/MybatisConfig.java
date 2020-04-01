package com.macro.mall.tiny.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zjd
 * @Description
 * @date 2020/3/30 12:32
 */

@Configuration
@MapperScan({"com.macro.mall.tiny.mbg.mapper","com.macro.mall.tiny.dao"})
public class MybatisConfig {

}
