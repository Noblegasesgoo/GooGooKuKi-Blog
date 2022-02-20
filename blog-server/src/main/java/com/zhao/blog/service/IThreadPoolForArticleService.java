package com.zhao.blog.service;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/21 20:55
 * @description 文章线程池类
 */
public interface IThreadPoolForArticleService {

    /**
     * redis中原子同步更新文章浏览数量
     * @param articleId
     */
    void updateArticleViewCount(Long articleId);

    /**
     * 初始化阅读数
     */
    void initViewCount();
}
