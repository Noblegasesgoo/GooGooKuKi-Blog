package com.zhao.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/18 11:56
 * @description 启动类
 */

@SpringBootApplication
public class BlogServerApplication {

    @PostConstruct
    void started() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogServerApplication.class, args);
    }
}
