package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.config.AppConfig;
import com.dlut.ResearchService.entity.dto.EmailDto;
import com.dlut.ResearchService.exception.BusinessException;
import com.dlut.ResearchService.utils.CreateImageCode;
import com.dlut.ResearchService.utils.StringUtils;
import com.dlut.ResearchService.entity.constants.EmailConstants;
import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.service.IEmailCodeService;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

/**
 * @author zsl
 * @since 2023-07-12
 */
@Service
public class EmailCodeServiceImpl implements IEmailCodeService {
    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private AppConfig appConfig;
    @Resource
    private RedisServiceImpl redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if (!email.matches(Regex.DLUT_MAIL)){
            throw new BusinessException("请使用大工邮箱，或检查您所输入的邮箱信息");
        }
        String code = StringUtils.getRandomNumber(EmailConstants.LENGTH_5);
        // 将邮箱与验证码存入Redis
        redisService.set(email, code);
        //TODO 发送验证码
        sendEmailCode(email, code);
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

            EmailDto emailDto = redisService.getEmailDto();

            helper.setSubject(emailDto.getRegisterMailTitle());
            helper.setText(String.format(emailDto.getRegisterEmailContent(), code));
            helper.setSentDate(new Date());

            javaMailSender.send(message);
        } catch (Exception e){
            logger.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败");
        }
    }

    public void getCaptcha(HttpServletResponse response, HttpSession session, Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130,38,5,10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0){
            session.setAttribute(EmailConstants.CAPTCHA, code);
        } else {
            session.setAttribute(EmailConstants.CAPTCHA_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }
}
