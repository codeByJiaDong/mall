package com.zjd.mall.security.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author zjd
 * @Description
 * @date 2020/4/15 10:53
 */

public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 获取spring 上下文
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }


    /**
     * 初始化赋值
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 通过 bean name find bean 原理反射
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class 获取 bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name，以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
