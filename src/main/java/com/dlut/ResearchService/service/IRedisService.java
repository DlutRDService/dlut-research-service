package com.dlut.ResearchService.service;

import org.springframework.data.redis.core.RedisCallback;

public interface IRedisService {
    void set(String key, String value);

    void set(String key, String value, Integer expirationTime);

    String get(String key);

    void delete(String key);

    boolean executeTransaction(RedisCallback<Boolean> callback);

    boolean acquireLock(String lockKey, Integer expirationTime);
}
