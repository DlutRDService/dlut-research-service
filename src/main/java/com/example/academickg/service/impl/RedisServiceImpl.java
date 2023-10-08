package com.example.academickg.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.academickg.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public void createRedisKey(List<Object> files, String keyName){
        String jsonStr = stringRedisTemplate.opsForValue().get(keyName);
        if (StrUtil.isBlank(jsonStr)){
            stringRedisTemplate.opsForValue().set(keyName, JSONUtil.toJsonStr(files));
        } else {
            files = JSONUtil.toBean(jsonStr, new TypeReference<>() {
            }, true);
        }
        new Result(StatusCode.STATUS_CODE_200, null, null);
    }
}
