package com.zjd.mall.security.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author zjd
 * @Description redis 配置类
 * @date 2020/4/16 10:50
 */
@EnableCaching
@Configuration
public class RedisConfig
{

    /**
     * 返回一个
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate
            (RedisConnectionFactory redisConnectionFactory){
        //初始化序列化器
        RedisSerializer<Object> serializer = redisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置redis 连接 ，没设置 还操作个 屁
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置 key 的 序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        //创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        //objectMapper 是一个 JSON 转换实体类的转换器
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper 设置对象的 可见性 -> 就是你要生成成 一个怎么样 存取的对象
        //第一个参数 存取器 就是 一个枚举 里面有对象 get set 有参 无参 不懂就所有啦
        //第二个参数 可见性 其实就是 对类 方法 属性的 可访问的 属性 设置所有
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //objectMapper 默认设置 就是生成 怎么样实体 ，选择最终的
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置Redis缓存有效期为1天
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer())).entryTtl(Duration.ofDays(1));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }
}
