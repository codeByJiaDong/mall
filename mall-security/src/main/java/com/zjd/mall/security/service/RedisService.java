package com.zjd.mall.security.service;


import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 批量删除属性
     * @param key
     * @return
     */
    Long del(List<String> key);

    /**
     * 设置过期时间
     * @param key
     * @param time
     * @return
     */
    Boolean expire(String key, long time);

    /**
     * 获取过期时间
     * @param key
     * @return
     */
    Long getExpire(String key);

    /**
     * 判断是否有该属性
     * @param key
     * @return
     */
    Boolean haskey(String key);

    /**
     * 按delta 递增
     * @param key
     * @param delta
     * @return
     */
    Long incr(String key, long delta);

    /**
     * 按delta 递减
     * @param key
     * @param delta
     * @return
     */
    Long decr(String key, long delta);

    /**
     * 获取hash结构中的属性
     * @param key
     * @param hashkey
     * @return
     */
    Object hGet(String key, String hashkey);

    /**
     * 向hash结构中放入一个属性
     * @param key
     * @param hashKey
     * @param value
     * @param time
     * @return
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     * @param key
     * @param hashKey
     * @param value
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     * @param key
     * @return
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 删除Hash结构中的属性
     * @param key
     * @param hashkey
     */
    void hDel(String key, Object... hashkey);

    /**
     * 判断Hash结构中是否该属性
     * @param key
     * @param hashKey
     * @return
     */
    Boolean hHashkey(String key, String hashKey);

    /**
     * Hash 结构中属性递增
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash 结构中属性递减
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 获取set 结构
     * @param key
     * @return
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set 结构中添加属性
     * @param key
     * @param values
     * @return
     */    Long sAdd(String key, Object... values);

    /**
     * 是否为Set中的属性
     * @param key
     * @param value
     * @return
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set 结构的长度
     * @param key
     * @return
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     * @param key
     * @param values
     * @return
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List结构中的属性
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List 结构的长度
     * @param key
     * @return
     */
    Long lSize(String key);

    /**
     * 根据索引获取List中的属性
     * @param key
     * @param index
     * @return
     */
    Object lIndex(String key, long index);

    /**
     * 向List结构中添加属性
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List结构中移除属性
     */
    Long lRemove(String key, long count, Object value);
}
