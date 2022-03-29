package com.zhao.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/21 20:58
 * @description 线程池配置类
 */

@Configuration
@EnableAsync(proxyTargetClass = true) /** 开启 cglib 代理 **/
public class ThreadPoolConfig {

    @Bean("taskExecutorForArticle")
    public Executor asyncArticleServiceExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        /** 设置核心线程数 **/
        threadPoolTaskExecutor.setCorePoolSize(3);
        /** 设置线程池最大线程容纳数量 **/
        threadPoolTaskExecutor.setMaxPoolSize(10);
        /** 设置阻塞队列最大容纳任务数量 **/
        threadPoolTaskExecutor.setQueueCapacity(100);
        /** 设置临时核心线程无任务后存活时间 **/
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        /** 设置线程池线程昵称前缀 **/
        threadPoolTaskExecutor.setThreadNamePrefix("noblegasesgoo-goo-blog");
        /** 设置关闭线程池是在所有任务完成之后 **/
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        /** 线程池初始化 **/
        threadPoolTaskExecutor.initialize();


        return threadPoolTaskExecutor;
    }

    @Bean("taskExecutorForLog")
    public Executor asyncLogServiceExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        /** 设置核心线程数 **/
        threadPoolTaskExecutor.setCorePoolSize(2);
        /** 设置线程池最大线程容纳数量 **/
        threadPoolTaskExecutor.setMaxPoolSize(10);
        /** 设置阻塞队列最大容纳任务数量 **/
        threadPoolTaskExecutor.setQueueCapacity(100);
        /** 设置临时核心线程无任务后存活时间 **/
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        /** 设置线程池线程昵称前缀 **/
        threadPoolTaskExecutor.setThreadNamePrefix("noblegasesgoo-goo-blog");
        /** 设置关闭线程池是在所有任务完成之后 **/
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        /** 线程池初始化 **/
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    @Bean("taskExecutorForMessage")
    public Executor asyncMessageServiceExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        /** 设置核心线程数 **/
        threadPoolTaskExecutor.setCorePoolSize(2);
        /** 设置线程池最大线程容纳数量 **/
        threadPoolTaskExecutor.setMaxPoolSize(10);
        /** 设置阻塞队列最大容纳任务数量 **/
        threadPoolTaskExecutor.setQueueCapacity(100);
        /** 设置临时核心线程无任务后存活时间 **/
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        /** 设置线程池线程昵称前缀 **/
        threadPoolTaskExecutor.setThreadNamePrefix("noblegasesgoo-goo-blog");
        /** 设置关闭线程池是在所有任务完成之后 **/
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        /** 线程池初始化 **/
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
