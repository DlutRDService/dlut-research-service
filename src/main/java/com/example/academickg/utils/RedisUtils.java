package com.example.academickg.utils;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component("redisUtils")
public class RedisUtils<V> {
    @Resource
    private RedisTemplate<String, V> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    public V getStringValue(String redis_key){
        ValueOperations<String, V> valueOps = redisTemplate.opsForValue();
        return redis_key == null ? null : valueOps.get(redis_key);
    }
    public Map<String, V> getEntities(String redis_key){
        HashOperations<String, String, V> hashOperations = redisTemplate.opsForHash();
        return redis_key == null ? null : hashOperations.entries(redis_key);
    }
    public List<Object> getHashValues(String redis_key, Collection<String> hashKeys){
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        return redis_key == null ? null : hashOps.multiGet(redis_key, Collections.singleton(hashKeys));
    }
    public List<Float> getHashValue(String redis_key, String hashKey){
        HashOperations<String ,String, List<Float>> hashOps = redisTemplate.opsForHash();
        return redis_key == null ? null : hashOps.get(redis_key, hashKey);
    }

    public boolean set(String redis_key, V value){
        try{
            redisTemplate.opsForValue().set(redis_key, value);
            return true;
        } catch (Exception e){
            logger.error("设置redis key: {}, value: {} 失败", redis_key, value);
            return false;
        }
    }

    public boolean setex(String redis_key, V value, long time){
        try {
            if (time>0) {
                redisTemplate.opsForValue().set(redis_key, value, time, TimeUnit.SECONDS);
            } else {
                set(redis_key, value);
            }
            return true;
        } catch (Exception e){
            logger.error("设置redis key: {}, value: {} 失败", redis_key, value);
            return false;
        }
    }

    public boolean setHash(String redis_key, Map<String, Vector<Float>> hashMap){
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        try{
            hashOps.putAll(redis_key, hashMap);
            return true;
        } catch (Exception e){
            logger.error("设置redis key: {} 失败", redis_key);
            return false;
        }
    }

}
