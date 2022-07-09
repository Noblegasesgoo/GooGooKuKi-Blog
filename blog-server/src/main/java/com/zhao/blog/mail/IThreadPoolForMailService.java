package com.zhao.blog.mail;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/7/9 18:22
 * @description 邮件线程池
 */
public interface IThreadPoolForMailService {

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     */
    void sendEmailForComment(String to, String subject);
}
