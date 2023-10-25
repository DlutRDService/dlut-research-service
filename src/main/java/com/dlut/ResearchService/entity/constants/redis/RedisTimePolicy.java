package com.dlut.ResearchService.entity.constants.redis;

public class RedisTimePolicy {
    public static final int CAPTCHA_LOCK_EXPIRATION_TIME = 60; // 锁过期时间，单位豪秒
    public static final int CAPTCHA_EXPIRATION_TIME = 300; // 验证码有效时间，单位秒
}
