package com.zhao.blog.common.annotations;

import java.lang.annotation.*;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 10:55
 * @description aop自定义日志注解
 */

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLogger {

    /** 标注操作方法 **/
    String method() default "";

    /** 标注操作模块 **/
    String module() default "";

    /** 标注操作操作描述 **/
    String operation() default "";

}
