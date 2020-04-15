package com.zjd.mall.security.annotation;


import java.lang.annotation.*;

/**
 * @author zjd
 * @Description 自定义注解，有此注解的缓存方法会抛出异常
 * @date 2020/4/15 10:51
 */
@Documented
// 放置在方法上
@Target(ElementType.METHOD)
// 默认设置在程序运行时扫描
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheException {
}
