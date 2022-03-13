package com.zhao.blog.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/18 19:52
 * @description 秒杀系统消息消费者
 */

@Service
@Slf4j
public class BlogArticleReceiver {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 异步更新文章详情信息消息
     * @param articleId
     */
    @RabbitListener(queues = "articleContentQueue")
    public void doDeleteArticleContentCache(String articleId){
        log.info("=====>>>>> 删除id为{}文章缓存开始执行...  {}", articleId, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        /** 延迟至数据库更新完毕再更新缓存 **/
        String params = DigestUtils.md5Hex(articleId);
        String redisKey = "article_content::ArticleController::queryArticleById::"+params;

        Boolean delete = redisTemplate.delete(redisKey);
        if (delete) {
            log.info("=====>>>>> id为{}文章缓存删除完毕！  {}", articleId, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        }

        log.info("=====>>>>> id为{}文章缓存删除过程中出现问题...  {}", articleId, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
    }
}
