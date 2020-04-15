package com.zjd.mall.security.service;


/**
 * @author zjd
 * @Description redis操作Service
 * @date 2020/4/15 15:39
 */

public interface RedisService
{
    /**
     * 保存属性
     * @param key
     * @param value
     * @param time 过期时间
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性 没有过期时间 内存满了就删除
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * 获取属性
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 删除属性
     * @param key
     * @return
     */
    Boolean del(String key);

    Long del()
}
