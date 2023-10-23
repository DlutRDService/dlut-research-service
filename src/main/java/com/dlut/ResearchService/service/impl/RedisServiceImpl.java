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


@Service
@Slf4j
public class RedisServiceImpl implements IRedisService {
    private static final String LOCK_KEY_PREFIX = "lock:send-verification-code:";
    private static final long LOCK_EXPIRATION_TIME = 5000; // 锁过期时间，单位毫秒

    @Resource
    private StringRedisTemplate redisTemplate;
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private EmailDto emailDto;

    @Override
    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }
    @Override
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key){
        redisTemplate.delete(key);
    }
    @Override
    public boolean executeTransaction(RedisCallback<Boolean> callback){
        return Boolean.TRUE.equals(redisTemplate.execute(callback));
    }
    @Override
    public boolean acquireLock(String lockKey) {
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked");
        if (lockAcquired != null && lockAcquired) {
            // 设置锁的过期时间，避免锁永久存在
            redisTemplate.expire(lockKey, LOCK_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    @Override
    public EmailDto getEmailDto(){
        RedisUtils<EmailDto> redisUtils = new RedisUtils<>();
        emailDto = redisUtils.getStringValue(RedisKey.REDIS_KEY_SYS_SETTING);
        if (null == emailDto){
            emailDto = new EmailDto();
            if(redisUtils.set(RedisKey.REDIS_KEY_SYS_SETTING, emailDto)) {
                log.info("设置Redis成功");
            }
        }
        return emailDto;
    }

}
