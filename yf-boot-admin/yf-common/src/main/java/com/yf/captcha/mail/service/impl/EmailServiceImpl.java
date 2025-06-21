package com.yf.captcha.mail.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.yf.captcha.mail.constants.FreemarkerConstants;
import com.yf.captcha.mail.constants.MailConstants;
import com.yf.captcha.mail.dto.SendCodeFtlDto;
import com.yf.captcha.mail.service.IEmailService;
import com.yf.configuration.CompanyConfiguration;
import com.yf.constants.RedisKeyConstants;
import com.yf.constants.SystemConstants;
import com.yf.log.common.ICommonOperateLogService;
import com.yf.model.log.entity.OperateLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.log.enums.LogOperatorStatusEnum;
import com.yf.model.log.enums.OperatorTypeEnum;
import com.yf.utils.RedisUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 发送邮箱服务实现类
 *
 * @author yiFei
 * @since 2024/3/8 12:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {
    // 防止报错
//    @Resource
//    private final JavaMailSender javaMailSender;

    private final CompanyConfiguration companyConfiguration;
    private final TaskExecutor ioIntensiveExecutor;
    private final RedisUtil redisUtil;
    private final Configuration configuration;
    private final ICommonOperateLogService commonOperateLogService;

    /**
     * 使用邮箱发送验证码
     *
     * @param email 需要送达的邮箱
     */
    @Override
    public void sendEmailCode(String email) {
        long enterTime = System.currentTimeMillis();
        // 1. 生成验证码
        String code = RandomUtil.randomString(MailConstants.EMAIL_CODE_NUM);
        // 2. 构建  sendCodeFtlDto 对象 （用于生成ftl的信息）
        SendCodeFtlDto sendCodeFtlDto = SendCodeFtlDto.builder()
                .companyName(companyConfiguration.getName())            // 公司名
                .companyWebsite(companyConfiguration.getWebsite())      // 公司官网
                .validTime(MailConstants.EMAIL_CODE_TIME_OUT)           // 有效时长
                .verifyCode(code).build();
        // 3. 通过 CompletableFuture 发送邮箱验证码
        CompletableFuture.runAsync(() -> {
            try (StringWriter stringWriter = new StringWriter()) {
                // 4. 构建 email_code.ftl 模板
                Template template = configuration.getTemplate(FreemarkerConstants.EMAIL_SEND_CODE_FTL_PATH);
                template.process(sendCodeFtlDto, stringWriter);

                // 5. 发送 email_code.html 文档
                sendEmailWithHtmlContent(email,
                        SystemConstants.EMAIL_CODE_TEMPLATE_SUBJECT.formatted(companyConfiguration.getName()),
                        stringWriter.toString()
                );
                // 6. 记录发送成功日志
                commonOperateLogService.asyncSaveOperateLog(getOperateLog(
                        LogOperatorStatusEnum.SUCCESS, null, null, System.currentTimeMillis() - enterTime));
            } catch (Exception e) {
                // 7. 记录发送失败日志
                commonOperateLogService.asyncSaveOperateLog(getOperateLog(
                        LogOperatorStatusEnum.ERROR, email, e.getMessage(), System.currentTimeMillis() - enterTime));
                throw new RuntimeException(e);
            }
        }, ioIntensiveExecutor).thenRunAsync(() -> {
            // 8. 将验证码存储到 redis ( 默认转大写 )
            redisUtil.addCacheZSetValue(RedisKeyConstants.EMAIL_CODE_CACHE_PREFIX + email
                    , code.toUpperCase(), Instant.now().toEpochMilli(), sendCodeFtlDto.getValidTime(), TimeUnit.MINUTES);
        }, ioIntensiveExecutor);
    }

    /**
     * 校验邮箱验证码
     *
     * @param email     邮箱
     * @param emailCode 验证码
     * @return 是否校验成功
     */
    @Override
    public boolean checkEmailCode(String email, String emailCode) {
        // 1. 获取当前时间
        Instant currentTime = Instant.now();
        // 2. 减去对应分钟数
        Instant adjustedTime = currentTime.minus(Duration.ofMinutes(MailConstants.EMAIL_CODE_TIME_OUT));
        // 3. 获取缓存中在3分钟之类的值
        String redisEmailCodeKey = RedisKeyConstants.EMAIL_CODE_CACHE_PREFIX + email;
        Set<String> cacheZSetByScore = redisUtil.getCacheZSetByScore(
                redisEmailCodeKey,              // redis 中存储key
                adjustedTime.toEpochMilli(),    // 开始时间 (三分钟前)
                currentTime.toEpochMilli(),     // 结束时间 (当前时间)
                0, -1
        );
        // 4. 校验是否存在该值 ( 默认转大写 )
        boolean result = cacheZSetByScore.contains(emailCode.toUpperCase());
        if (result) {
            // 5. 验证成功 删除 redis 缓存数据防止二次使用
            redisUtil.deleteObject(redisEmailCodeKey);
        }
        return result;
    }


    /**
     * 发送包含 HTML 内容的邮件
     *
     * @param sendToEmail 收件人邮箱地址
     * @param subject     邮件主题
     * @param html        HTML 格式的邮件内容
     * @throws MessagingException 发送邮件过程中可能抛出的异常
     */
    private void sendEmailWithHtmlContent(String sendToEmail, String subject, String html) throws MessagingException {
        // 创建 MimeMessage 对象
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessage mimeMessage = null;
        // 使用 MimeMessageHelper 对象设置邮件的发送者、接收者、主题和内容
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(companyConfiguration.getName() + '<' + companyConfiguration.getEmail() + ">");
        mimeMessageHelper.setTo(sendToEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(html, true);

        // 发送邮件
//        javaMailSender.send(mimeMessage);
    }


    private OperateLog getOperateLog(LogOperatorStatusEnum status, String param, String errorMsg, Long costTime) {
        return OperateLog.builder()
                .title(BusinessTypeEnum.SEND_EMAIL.getLabel())
                .businessType(BusinessTypeEnum.SEND_EMAIL)
                .operatorType(OperatorTypeEnum.MANAGE)
                .operatorParam(param)
                .status(status)
                .errorMsg(errorMsg)
                .costTime(costTime)
                .method("sendEmailCode")
                .build();
    }
}
