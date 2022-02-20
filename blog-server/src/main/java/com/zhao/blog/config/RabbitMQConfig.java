package com.zhao.blog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/17 20:51
 * @description RabbitMQ配置类
 */

@Configuration
public class RabbitMQConfig {

    private static final String ARTICLE_CONTENT_QUEUE = "articleContentQueue";

    private static final String ARTICLE_EXCHANGE = "articleEXCHANGE";

    @Bean
    public Queue articleContentQueue() {
        return new Queue(ARTICLE_CONTENT_QUEUE, true);
    }

    @Bean
    public TopicExchange articleEXCHANGE() {
        return new TopicExchange(ARTICLE_EXCHANGE);
    }

    @Bean
    public Binding orderQueueBind() {
        return BindingBuilder.bind(articleContentQueue()).to(articleEXCHANGE()).with("googookuki.blog.article");
    }

}
