package com.zjd.mall.security.aspect;


import com.zjd.mall.security.annotation.CacheException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zjd
 * @Description Redis 缓存切面，防止Redis宕机影响正常的业务逻辑
 * @date 2020/4/15 15:11
 */
@Aspect
@Component
//这个注解 是 配置 加载bean 的优先级 数字越小，优先级越高
@Order(2)
@Slf4j
public class RedisCacheAspect
{
    /**
     * 对那些包进行切面
     */
    @Pointcut(value = "execution(public * com.macro.mall.portal.service.*CacheService.*(..)) || execution(public * com.macro.mall.service.*CacheService.*(..))")
    public void cacheAspect(){}

    /**
     * 环绕通知 就是对这个方法进行代理 操作
     * @param joinPoint 切点 -> 就 拦截到的方法class
     * @return
     * @throws Throwable
     */
    @Around(value = "cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //切点 获取 数字签名  ——> 应该是字节码class
        Signature signature = joinPoint.getSignature();
        //把签名强转成 方法签名
        MethodSignature methodSignature = (MethodSignature)signature;
        //获取class 文件的方法
        Method method = methodSignature.getMethod();
        Object result = null;
        try {
            //执行方法呀
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            //这里是通过获取方法实例 去找的
            //有CacheException注解的就抛出异常
            if (method.isAnnotationPresent(CacheException.class)){
                throw throwable;
            }else {
                log.error(throwable.getMessage());
            }
        }
        return result;
    }
}
