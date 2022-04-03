package com.zhao.blog.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhao.blog.mapper.ArticleMapper;
import com.zhao.blog.vo.ArticleOptimizeVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/12 14:00
 * @description ES数据定时任务
 */

@Component
@Slf4j
@EnableScheduling
public class EsHandler {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private ArticleMapper articleMapper;

    /** 每天凌晨一点进行一次任务 **/
    @Scheduled(cron = "0 0 1 * * ?")
    @Async("taskExecutorForArticle")
    public void updateEsData() throws IOException {
        log.info("=====>>>>> 同步查询数据开始执行...  {}", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        /** 删除index下的所有内容再更新 **/
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("googookukiblogarticlesindex");
        AcknowledgedResponse deleteIndexRes = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        /** 从数据库中查询当前最新数据 **/
        List<ArticleOptimizeVo> articleOptimizeVos = articleMapper.selectArticleForEs();

        /** 循环遍历同步到es中 **/
        int size = articleOptimizeVos.size();
        for (int i = 0; i < size; i++) {

            IndexRequest indexRequest = new IndexRequest("googookukiblogarticlesindex");
            indexRequest.id(articleOptimizeVos.get(i).getId().toString());
            indexRequest.source(objectMapper.writeValueAsString(articleOptimizeVos.get(i)), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }

        log.info("=====>>>>> 同步查询数据结束  {}", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
    }
}
