package com.zhao.blog.mail.impl;

import com.zhao.blog.mail.IMailService;
import com.zhao.blog.mail.IThreadPoolForMailService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 发送评论文本邮件
     *
     * @param to      收件人
     * @param content 内容
     * @param subject 主题
     */
    @Override
    public void sendEmailForComment(String to, String content, String subject) {
        mailService.sendSimpleMail(to, content, subject);
    }
}
