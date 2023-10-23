package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.dto.EmailDto;
import org.springframework.data.redis.core.RedisCallback;

public interface IRedisService {
    void set(String key, String value);

    String get(String key);

    void delete(String key);

    boolean executeTransaction(RedisCallback<Boolean> callback);

    boolean acquireLock(String lockKey);


    EmailDto getEmailDto();
}
