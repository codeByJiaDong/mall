package com.zjd.mall.security.service.impl;


import com.zjd.mall.security.service.RedisService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zjd
 * @Description
 * @date 2020/4/16 10:49
 */

public class RedisServiceImpl implements RedisService
{

    @Override
    public void set(String key, Object value, long time) {

    }

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public Boolean del(String key) {
        return null;
    }

    @Override
    public Long del(List<String> key) {
        return null;
    }

    @Override
    public Boolean expire(String key, long time) {
        return null;
    }

    @Override
    public Long getExpire(String key) {
        return null;
    }

    @Override
    public Boolean haskey(String key) {
        return null;
    }

    @Override
    public Long incr(String key, long delta) {
        return null;
    }

    @Override
    public Long decr(String key, long delta) {
        return null;
    }

    @Override
    public Object hGet(String key, String hashkey) {
        return null;
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        return null;
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {

    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return null;
    }

    @Override
    public void hDel(String key, Object... hashkey) {

    }

    @Override
    public Boolean hHashkey(String key, String hashKey) {
        return null;
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return null;
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return null;
    }

    @Override
    public Set<Object> sMembers(String key) {
        return null;
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return null;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return null;
    }

    @Override
    public Long sSize(String key) {
        return null;
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return null;
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return null;
    }

    @Override
    public Long lSize(String key) {
        return null;
    }

    @Override
    public Object lIndex(String key, long index) {
        return null;
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        return null;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return null;
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        return null;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return null;
    }
}
