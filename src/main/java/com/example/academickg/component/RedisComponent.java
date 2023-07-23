package com.example.academickg.component;

import com.example.academickg.entity.constants.RedisKey;
import com.example.academickg.entity.dto.SysSettingsDto;
import com.example.academickg.entity.dto.PaperVectorDto;
import com.example.academickg.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Vector;


@Component("redisComponent")
public class RedisComponent {
    private static final Logger logger = LoggerFactory.getLogger(RedisComponent.class);
    @Resource
    private PaperVectorDto paperVectorDto;
    @Resource
    private SysSettingsDto sysSettingsDto;
    public SysSettingsDto getSysSettingDto(){
        RedisUtils<SysSettingsDto> redisUtils = new RedisUtils<>();
        sysSettingsDto = redisUtils.getStringValue(RedisKey.REDIS_KEY_SYS_SETTING);
        if (null == sysSettingsDto){
            sysSettingsDto = new SysSettingsDto();
            if(redisUtils.set(RedisKey.REDIS_KEY_SYS_SETTING, sysSettingsDto)) {
                logger.info("设置Redis成功");
            }
        }
        return sysSettingsDto;
    }

    public PaperVectorDto getPaperVectorDto(){
        RedisUtils<Vector<Float>> redisUtils = new RedisUtils<>();
        paperVectorDto.map = redisUtils.getEntities(RedisKey.REDIS_KEY_PAPER_VECTOR);
        if (null == paperVectorDto){
            paperVectorDto = new PaperVectorDto();
            //List<String> valueList = paperVectorDto.getMap().keySet().stream().toList();
            if(redisUtils.setHash(RedisKey.REDIS_KEY_PAPER_VECTOR, paperVectorDto.getMap())){
                logger.info("设置redis成功");
            }
        }
        return paperVectorDto;
    }
}
