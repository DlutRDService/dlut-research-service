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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static com.dlut.ResearchService.entity.constants.redis.RedisKey.LOCK_KEY_PREFIX;
import static com.dlut.ResearchService.entity.constants.redis.RedisTimePolicy.CAPTCHA_EXPIRATION_TIME;
import static com.dlut.ResearchService.entity.constants.redis.RedisTimePolicy.CAPTCHA_LOCK_EXPIRATION_TIME;


/**
 * @author zsl
 * @since 2023-07-12
 */
@Slf4j
@Service
public class EmailCodeServiceImpl implements IEmailCodeService {
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private AppConfig appConfig;
    @Resource
    private RedisServiceImpl redisService;

    /**
     * 向邮箱发送验证码
     * @param email 目标邮箱
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(@NotNull String email) {
        if (!email.matches(Regex.DLUT_MAIL)){
            throw new BusinessException("请使用大工校园邮箱，检查您所输入的邮箱信息");
        }
        boolean lockAcquired = redisService.acquireLock(LOCK_KEY_PREFIX, CAPTCHA_LOCK_EXPIRATION_TIME);
        String emailCode;
        if (lockAcquired) {
            emailCode = StringUtils.getRandomNumber(EmailConstants.LENGTH_5);
            redisService.set(email, emailCode, CAPTCHA_EXPIRATION_TIME);
        }else {
            emailCode = redisService.get(email);
        }
        sendEmailCode(email, emailCode);
    }
    /**
     * 邮箱发送
     * @param toEmail 发送地址
     * @param emailCode 邮箱验证码
     */
    public void sendEmailCode(String toEmail, String emailCode){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(appConfig.getSendUserName());
            helper.setTo(toEmail);

            EmailDto emailDto = new EmailDto();

            helper.setSubject(emailDto.getRegisterMailTitle());
            helper.setText(String.format(emailDto.getRegisterEmailContent(), emailCode));
            helper.setSentDate(new Date());

            javaMailSender.send(message);
        } catch (Exception e){
            log.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败");
        }
    }

    /**
     * 生成图片验证码
     */
    public void getCaptcha(@NotNull HttpServletResponse response, @NotNull HttpSession session) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130,38,5,10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        String code = vCode.getCode();
        session.setAttribute(EmailConstants.CAPTCHA, code);

        vCode.write(response.getOutputStream());
    }

    /**
     * 检查图片验证码是否正确
     * @param captcha 图片验证码
     */
    public void checkCaptcha(@NotNull HttpSession session, @NotNull String captcha){
        try{
            if (!captcha.equalsIgnoreCase((String) session.getAttribute(EmailConstants.CAPTCHA))){
                throw new BusinessException("图片验证码不正确，请刷新后重新输入");
            }
        } finally {
            session.removeAttribute(EmailConstants.CAPTCHA);
        }
    }
}
