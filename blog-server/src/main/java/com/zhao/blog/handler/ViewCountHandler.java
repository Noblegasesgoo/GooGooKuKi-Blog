package com.zhao.blog.handler;

import com.zhao.blog.domain.entity.Article;
import com.zhao.blog.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/28 14:28
 * @description 阅读数定时任务处理器
 */

@Component
@Slf4j
@EnableScheduling
public class ViewCountHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private ArticleMapper articleMapper;

    /** 每隔十秒进行一次任务 **/
    @Scheduled(cron = "*/10 * * * * ?")
    @Async("taskExecutorForArticle")
    public void viewCountScheduled(){
        log.info("=====>>>>> 同步浏览量开始执行...  {}", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        /** 从 redis 中获取当前最新的观看数 **/
        Map<Object, Object> countMap = redisTemplate.opsForHash().entries("view_count");

        /** 循环遍历同步到数据库中 **/
        for (Map.Entry<Object,Object> entry : countMap.entrySet()){

            Long articleId = Long.parseLong(entry.getKey().toString());
            Integer count = Integer.parseInt(entry.getValue().toString());

            /** 创建更新条件 **/
            Article article = new Article();
            article.setId(articleId);
            article.setViewCounts(count);
            articleMapper.updateById(article);
        }

        log.info("=====>>>>> 同步浏览量结束！  {}", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
    }
}
