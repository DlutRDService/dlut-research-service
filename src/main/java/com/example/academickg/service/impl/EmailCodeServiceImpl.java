package com.example.academickg.service.impl;

import com.example.academickg.component.RedisComponent;
import com.example.academickg.entity.constants.EmailConstants;
import com.example.academickg.config.AppConfig;
import com.example.academickg.entity.constants.Regex;
import com.example.academickg.entity.dao.EmailCode;
import com.example.academickg.entity.dto.SysSettingsDto;
import com.example.academickg.exception.BusinessException;
import com.example.academickg.mapper.EmailCodeMapper;
import com.example.academickg.mapper.UserInfoMapper;
import com.example.academickg.service.IEmailCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.example.academickg.utils.StringUtils;



/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
@Service
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode> implements IEmailCodeService {
    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private EmailCodeMapper emailCodeMapper;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private AppConfig appConfig;
    @Resource
    private RedisComponent redisComponent;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if (!email.matches(Regex.DLUT_MAIL)){
            throw new BusinessException("请使用大工邮箱，或检查您所输入的邮箱信息");
        }
//        if (Objects.equals(type, Constants.ZERO)){
//            if (null != userInfoMapper.selectByEmail(email)){
//                throw new BusinessException("邮箱已被占用");
//            }
//        }
        String code = StringUtils.getRandomNumber(EmailConstants.LENGTH_5);
        //TODO 发送验证码
        sendEmailCode(email, code);

        // 将之前的值设置为无效
        emailCodeMapper.disableEmailCode(email);

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(EmailConstants.ZERO);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }

    /**
     * 邮箱发送
     * @param toEmail 发送地址
     * @param code    验证码
     */
    public void sendEmailCode(String toEmail, String code){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(appConfig.getSendUserName());
            helper.setTo(toEmail);

            SysSettingsDto sysSettingsDto = redisComponent.getSysSettingDto();

            helper.setSubject(sysSettingsDto.getRegisterMailTitle());
            helper.setText(String.format(sysSettingsDto.getRegisterEmailContent(), code));
            helper.setSentDate(new Date());

            javaMailSender.send(message);
        } catch (Exception e){
            logger.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败");
        }

    }
}
