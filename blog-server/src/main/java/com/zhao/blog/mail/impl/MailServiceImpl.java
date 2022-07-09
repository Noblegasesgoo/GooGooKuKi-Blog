package com.zhao.blog.mail.impl;

import com.zhao.blog.mail.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/7/9 17:00
 * @description 邮件发送服务实现类
 */

@Slf4j
@Service
public class MailServiceImpl implements IMailService {


    /**
     * Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
     */
    @Resource
    private JavaMailSender mailSender;

    /**
     * 配置文件中我的qq邮箱
     */
    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送评论文本邮件
     *
     * @param to      收件人
     * @param content 内容
     * @param subject 主题
     */
    @Override
    public void sendSimpleMail(String to, String content, String subject) {
        // 创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 邮件发送人
        message.setFrom(from);
        // 邮件接收人
        message.setTo(to);
        // 邮件主题
        message.setSubject(subject);
        // 邮件内容
        message.setText(content);
        // 发送邮件
        mailSender.send(message);
    }
}
