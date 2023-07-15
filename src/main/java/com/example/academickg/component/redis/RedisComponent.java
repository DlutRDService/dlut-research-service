package com.example.academickg.component.redis;

import com.example.academickg.entity.constants.RedisKey;
import com.example.academickg.entity.dto.SysSettingsDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


@Component("redisComponent")
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;
    public SysSettingsDto getSysSettingDto(){
        SysSettingsDto sysSettingsDto = (SysSettingsDto) redisUtils.get(RedisKey.REDIS_KEY_SYS_SETTING);
        if (null == sysSettingsDto){
            sysSettingsDto = new SysSettingsDto();
            redisUtils.set(RedisKey.REDIS_KEY_SYS_SETTING, sysSettingsDto);
        }
        return sysSettingsDto;
    }
}
