package com.example.academickg.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.academickg.component.ResultBuilder;
import com.example.academickg.entity.constants.StatusCode;
import com.example.academickg.service.IRedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl implements IRedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ResultBuilder resultBuilder;
    public void createRedisKey(List<Object> files, String keyName){
        String jsonStr = stringRedisTemplate.opsForValue().get(keyName);
        if (StrUtil.isBlank(jsonStr)){
            stringRedisTemplate.opsForValue().set(keyName, JSONUtil.toJsonStr(files));
        } else {
            files = JSONUtil.toBean(jsonStr, new TypeReference<>() {
            }, true);
        }
        resultBuilder.build(StatusCode.STATUS_CODE_200, null, null);
    }
}
