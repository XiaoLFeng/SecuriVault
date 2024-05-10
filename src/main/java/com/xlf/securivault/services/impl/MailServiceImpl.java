/*
 * *******************************************************************************
 * Copyright (c) 2016-NOW(至今) 筱锋
 * Author: 筱锋(https://www.x-lf.com)
 *
 * 本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * *******************************************************************************
 * 许可证声明：
 *
 * 版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 * 本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 * 对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 * 作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 * 由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 * 由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 * 一定不可以抄袭，尤其是同校同学！！！
 * 你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 * 同维护好开源的社区环境！！！
 *
 * 使用本软件即表示您了解此声明并同意其条款。
 *
 * 有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 * https://opensource.org/licenses/MIT
 * *******************************************************************************
 * 免责声明：
 *
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

package com.xlf.securivault.services.impl;

import com.xlf.securivault.dao.EmailVerifyCodeDAO;
import com.xlf.securivault.dao.UserDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.exceptions.library.MailTemplateNotFoundException;
import com.xlf.securivault.models.entity.EmailVerifyCodeDO;
import com.xlf.securivault.models.entity.UserDO;
import com.xlf.securivault.services.MailService;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.Util;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件服务
 * <hr/>
 * 用于定义邮件服务，用于定义邮件服务；用于处理邮件相关的服务；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    /**
     * 用户数据访问对象
     */
    private final UserDAO userDAO;
    /**
     * 邮件验证码数据访问对象
     */
    private final EmailVerifyCodeDAO emailVerifyCodeDAO;
    /**
     * 发送邮件的准备数据
     */
    private final JavaMailSender javaMailSender;
    /**
     * 发送邮件的准备数据
     */
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String getValueMailFrom;

    /**
     * 发送邮件
     * <hr/>
     * 用于发送邮件；
     *
     * @param email     邮箱
     * @param template  模板
     * @param parameter 参数（参数请勿创建 title 内容，引用此函数将会重复创建）
     */
    @Async
    @Override
    public void sendMail(String email, String template, Map<String, Object> parameter) {
        ClassPathResource classPathResource = new ClassPathResource("templates/mail/" + template + ".html");
        if (!classPathResource.exists()) {
            throw new MailTemplateNotFoundException(template);
        }
        // 模板进一步加工
        parameter.put("title", "SecuriVault");
        // 创建邮件发送内容
        MimeMessage message = javaMailSender.createMimeMessage();
        Context context = new Context();
        context.setVariables(parameter);
        String getPatch = classPathResource.getPath().replace("templates/", "");
        String emailContent = templateEngine.process(getPatch, context);
        // 发送邮件
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(getValueMailFrom);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(parameter.get("title").toString());
            mimeMessageHelper.setText(emailContent, true);
            javaMailSender.send(message);
            log.debug("[MAIL] 发送邮件 {} 标题 {} 成功", email, parameter.get("title").toString());
        } catch (MessagingException e) {
            throw new MailSendException("发送邮件失败", e);
        }
    }

    /**
     * 发送重置密码邮件
     * <hr/>
     * 用于发送重置密码邮件；根据邮件模板和邮件接收者发送重置密码邮件；
     *
     * @param mailTo 邮件接收者
     */
    @Override
    public void sendResetPasswordMail(String mailTo, HttpServletRequest request) {
        // 获取邮件对应用户
        UserDO getUser = userDAO.getUserByEmail(mailTo);
        if (getUser != null) {
            // 检查是否可以再次发送邮件
            EmailVerifyCodeDO getEmailVerifyCode = emailVerifyCodeDAO.getLastVerifyCodeByEmail(mailTo);
            if (getEmailVerifyCode != null) {
                // 检查创建时间是否超过两分钟
                if (getEmailVerifyCode.getCreatedAt().getTime() + 120000 > System.currentTimeMillis()) {
                    long timeLeave =
                            (getEmailVerifyCode.getCreatedAt().getTime() + 120000 - System.currentTimeMillis()) / 1000;
                    throw new BusinessException("可重新发送时间 " + timeLeave + " 秒", ErrorCode.VERIFY_CODE_ERROR);
                } else {
                    // 删除验证码
                    emailVerifyCodeDAO.removeById(getEmailVerifyCode.getId());
                }
            }
            // 生成随机验证码
            String generateCode = Util.generateRandString(10);
            // 保存验证码
            EmailVerifyCodeDO newEmailVerifyCode = new EmailVerifyCodeDO();
            newEmailVerifyCode
                    .setEmail(mailTo)
                    .setCode(generateCode)
                    .setCreatedAt(new Timestamp(System.currentTimeMillis()))
                    .setExpiredAt(new Timestamp(System.currentTimeMillis() + 900000L));
            emailVerifyCodeDAO.save(newEmailVerifyCode);
            // 生成 Map 信息
            HashMap<String, Object> parameter = new HashMap<>();
            StringBuffer createLink = new StringBuffer(request.getScheme());
            createLink.append("://");
            createLink.append(request.getServerName());
            if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                createLink.append(":");
                createLink.append(request.getServerPort());
            }
            createLink.append("/api/v1/auth/reset-password?code=");
            createLink.append(generateCode);
            parameter.put("link", createLink);
            parameter.put("email", mailTo);
            parameter.put("username", getUser.getUsername());
            // 发送邮件
            sendMail(mailTo, "user-reset-password", parameter);
        } else {
            throw new BusinessException("用户不存在", ErrorCode.USER_NOT_EXIST);
        }
    }
}
