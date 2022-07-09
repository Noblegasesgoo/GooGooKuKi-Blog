package com.zhao.blog.mail;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/7/9 16:59
 * @description 邮件发送服务
 */

public interface IMailService {

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     */
    void sendSimpleMail(String to, String subject);

    ///**
    // * 发送HTML邮件
    // * @param to 收件人
    // * @param subject 主题
    // * @param content 内容
    // */
    //void sendHtmlMail(String to, String subject, String content);
    //
    //
    ///**
    // * 发送带附件的邮件
    // * @param to 收件人
    // * @param subject 主题
    // * @param content 内容
    // * @param filePath 附件
    // */
    // void sendAttachmentsMail(String to, String subject, String content, String filePath);
}
