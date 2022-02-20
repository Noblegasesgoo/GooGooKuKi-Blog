package com.zhao.blog.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/18 19:52
 * @description 秒杀系统消息生产者
 */

@Service
@Slf4j
public class BlogArticleSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 异步更新文章详情缓存消息（数据库 -> 缓存）
     * @param articleId
     */
    public void toDoDeleteArticleContentCache(Long articleId) {
        rabbitTemplate.convertAndSend("articleEXCHANGE", "googookuki.blog.article", articleId.toString());
    }

}
