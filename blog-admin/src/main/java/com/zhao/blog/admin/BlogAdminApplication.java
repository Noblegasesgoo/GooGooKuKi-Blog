package com.zhao.blog.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/6 11:27
 * @description 启动类
 */

@SpringBootApplication
@MapperScan("com.zhao.blog.admin.mapper")
public class BlogAdminApplication {

    @PostConstruct
    void started() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
    }

    public static void main(String[] args) {

        SpringApplication.run(BlogAdminApplication.class, args);
    }
}
