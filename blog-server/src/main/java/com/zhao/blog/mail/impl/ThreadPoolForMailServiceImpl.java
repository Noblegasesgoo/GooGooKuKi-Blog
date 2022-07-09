package com.zhao.blog.mail.impl;

import com.zhao.blog.mail.IMailService;
import com.zhao.blog.mail.IThreadPoolForMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/7/9 18:26
 * @description 邮件线程池服务
 */

@Service
public class ThreadPoolForMailServiceImpl implements IThreadPoolForMailService {

    @Autowired
    private IMailService mailService;

    /**
     * 发送文本邮件
     * @param to      收件人
     * @param subject 主题
     */
    @Async("taskExecutorForMail")
    @Override
    public void sendEmailForComment(String to, String subject) {
        mailService.sendSimpleMail(to, subject);
    }
}
