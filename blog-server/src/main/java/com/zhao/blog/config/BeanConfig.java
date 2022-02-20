package com.zhao.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/12 13:41
 * @description 各种自定义的bean配置
 */

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
