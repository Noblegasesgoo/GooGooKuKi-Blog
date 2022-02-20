package com.zhao.blog.common.annotations;

import java.lang.annotation.*;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/28 11:37
 * @description 统一缓存注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyCache {

    /** 缓存名称 **/
    String name() default "";

    /** 缓存过期时间 **/
    long expire() default 5 * 60 * 1000;
}
