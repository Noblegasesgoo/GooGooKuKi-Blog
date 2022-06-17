package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.zhao.blog.domain.entity.Article;
import com.zhao.blog.mapper.ArticleMapper;
import com.zhao.blog.service.IThreadPoolForArticleService;
import com.zhao.blog.vo.ArticleOptimizeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.joda.time.DateTime;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/21 20:55
 * @description 文章线程池
 */

@Slf4j
@Service
public class ThreadPoolForArticleServiceImpl implements IThreadPoolForArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initViewCount() {

        /** 保证项目启动时，redis 中如果没有浏览量的记录，那么就读取数据库进行 redis 的初始化 **/
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<>());

        /** 查询所有文章的阅读数 **/
        for (Article article : articles) {
            String viewCountStr = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(article.getId()));
            /** 如果 redis 中没有缓存，就初始化 redis **/
            if (StringUtils.isBlank(viewCountStr)){
                /** 进行初始化 **/
                redisTemplate.opsForHash().put("view_count", String.valueOf(article.getId()),String.valueOf(article.getViewCounts()));
            }
        }
    }

    /**
     * 初始化es中的index
     * @throws IOException
     */
    @PostConstruct
    public void initArticleDataIndexForEs() throws IOException {

        /** 首先查询是否存在该index **/
        GetIndexRequest getIndexRequest = new GetIndexRequest("googookukiblogarticlesindex");
        //GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        //GetIndexResponse getIndexResponse = restHighLevelClient.indices().get
        Boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        if (exists) {

            log.info("[goo-blog|ThreadPoolForArticleServiceImpl|initArticleDataIndexForEs] index已经存在，结束初始化！");
            return;
        }

        /** 否则就在初始化时新建该index **/
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("googookukiblogarticlesindex");
        //restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        log.info("[goo-blog|ThreadPoolForArticleServiceImpl|initArticleDataIndexForEs] 索引是否初始化新建成功：" + createIndexResponse.isAcknowledged());

        log.info("[goo-blog|ThreadPoolForArticleServiceImpl|initArticleDataIndexForEs] 更新index所指内容...");
        /** 首先从数据库查询数据 **/
        List<ArticleOptimizeVo> articleOptimizeVos = articleMapper.selectArticleForEs();

        /** 将数据存入es中 **/
        int size = articleOptimizeVos.size();
        for (int i = 0; i < size; i++) {

            /** 针对 googookukiblogarticlesindex index 存入数据 **/
            IndexRequest request = new IndexRequest("googookukiblogarticlesindex");
            request.id(articleOptimizeVos.get(i).getId().toString());
            request.source(objectMapper.writeValueAsString(articleOptimizeVos.get(i)), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }
        log.info("[goo-blog|ThreadPoolForArticleServiceImpl|initArticleDataIndexForEs] index所指内容更新完毕！");
    }

    ///**
    // * 更新es中的内容
    // * @throws IOException
    // */
    //@PostConstruct
    //public void syncDataToEs() throws IOException {
    //
    //    log.info("[goo-blog|ThreadPoolForArticleServiceImpl|syncDataToEs] 更新index所指内容...");
    //    /** 首先从数据库查询数据 **/
    //    List<ArticleOptimizeVo> articleOptimizeVos = articleMapper.selectArticleForEs();
    //
    //    /** 将数据存入es中 **/
    //    int size = articleOptimizeVos.size();
    //    for (int i = 0; i < size; i++) {
    //
    //        /** 针对 googookukiblogarticlesindex index 存入数据 **/
    //        IndexRequest request = new IndexRequest("googookukiblogarticlesindex");
    //        request.id(articleOptimizeVos.get(i).getId().toString());
    //        request.source(objectMapper.writeValueAsString(articleOptimizeVos.get(i)), XContentType.JSON);
    //        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
    //    }
    //    log.info("[goo-blog|ThreadPoolForArticleServiceImpl|syncDataToEs] index所指内容更新完毕！");
    //
    //}

    /**
     * redis中原子同步更新文章浏览数量
     * @param articleId
     */
    @Async("taskExecutorForArticle")
    @Override
    public void updateArticleViewCount (Long articleId) {
        ///** 期望操作通过线程池来完成不会影响原有主线程的查操作 **/
        //
        ///** 通过原子类CAS操作实现阅读数量的原子性递增 **/
        //AtomicInteger atomicInteger = new AtomicInteger(article.getViewCounts());
        //atomicInteger.getAndIncrement();
        //Article articleForUpdate = new Article();
        //articleForUpdate.setViewCounts(atomicInteger.get());
        //
        ///** 创建条件 **/
        //LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //articleLambdaQueryWrapper.eq(Article::getId, article.getId());
        //articleLambdaQueryWrapper.eq(Article::getViewCounts, article.getViewCounts());
        //
        ///** 开始执行更新操作 **/
        //articleMapper.update(articleForUpdate, articleLambdaQueryWrapper);
        redisTemplate.opsForHash().increment("view_count",String.valueOf(articleId),1);
    }

    /**
     * 异步更新文章详情信息消息
     * @param articleId
     */
    @Async("taskExecutorForArticle")
    @RabbitListener(queues = "articleContentQueue")
    public void doDeleteArticleContentCache(Channel channel, Message message, String articleId) throws InterruptedException {
        log.info("=====>>>>> 删除id为{}文章缓存开始执行...  {}", articleId, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        /** 当前线程延迟一秒 **/
        TimeUnit.SECONDS.sleep(1);

        /** 延迟至数据库更新完毕再更新缓存 **/
        String params = DigestUtils.md5Hex(articleId);
        String redisKey = "article_content:queryArticleById:"+params;

        Boolean delete = redisTemplate.delete(redisKey);
        if (delete) {
            /** 手动ack **/
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.info("=====>>>>> id为{}文章缓存删除完毕！  {}", articleId, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        }

        log.info("=====>>>>> id为{}文章缓存删除过程中出现问题...  {}", articleId, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
    }
}
