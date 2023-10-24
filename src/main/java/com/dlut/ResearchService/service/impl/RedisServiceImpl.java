package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.RedisKey;
import com.dlut.ResearchService.entity.dto.EmailDto;
import com.dlut.ResearchService.service.IRedisService;
import com.dlut.ResearchService.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {
    private static final String LOCK_KEY_PREFIX = "lock:send-verification-code:";
    private static final long LOCK_EXPIRATION_TIME = 5000; // 锁过期时间，单位毫秒

    @Resource
    private StringRedisTemplate stringRedisTemplater;
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private EmailDto emailDto;

    @Override
    public void set(String key, String value){
        stringRedisTemplater.opsForValue().set(key, value);
        stringRedisTemplater.expire(key, 300, TimeUnit.SECONDS);
    }
    @Override
    public String get(String key){
        return stringRedisTemplater.opsForValue().get(key);
    }

    @Override
    public void delete(String key){
        stringRedisTemplater.delete(key);
    }
    @Override
    public boolean executeTransaction(RedisCallback<Boolean> callback){
        return Boolean.TRUE.equals(stringRedisTemplater.execute(callback));
    }
    @Override
    public boolean acquireLock(String lockKey) {
        Boolean lockAcquired = stringRedisTemplater.opsForValue().setIfAbsent(lockKey, "locked");
        if (lockAcquired != null && lockAcquired) {
            // 设置锁的过期时间，避免锁永久存在
            stringRedisTemplater.expire(lockKey, LOCK_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

}
