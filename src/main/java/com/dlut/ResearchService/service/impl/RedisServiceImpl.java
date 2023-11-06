package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.service.IRedisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.dlut.ResearchService.entity.constants.redis.RedisKey.*;
import static com.dlut.ResearchService.entity.constants.redis.RedisTimePolicy.CAPTCHA_LOCK_EXPIRATION_TIME;

@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public void set(String key, String value) {

    }

    @Override
    public void set(String key, String value, Integer expirationTime){
        boolean lockAcquired = acquireLock(LOCK_KEY_PREFIX, CAPTCHA_LOCK_EXPIRATION_TIME);
        if (lockAcquired) {
            stringRedisTemplate.opsForValue().set(key, value);
            stringRedisTemplate.expire(key, expirationTime, TimeUnit.SECONDS);
        }
    }
    @Override
    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }
    @Override
    public boolean executeTransaction(RedisCallback<Boolean> callback){
        return Boolean.TRUE.equals(stringRedisTemplate.execute(callback));
    }
    @Override
    public boolean acquireLock(String lockKey, Integer expirationTime) {
        Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "locked");
        if (lockAcquired != null && lockAcquired) {
            stringRedisTemplate.expire(lockKey, expirationTime, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }
}
