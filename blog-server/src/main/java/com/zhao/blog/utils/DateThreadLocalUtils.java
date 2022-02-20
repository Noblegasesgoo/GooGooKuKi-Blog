package com.zhao.blog.utils;

import java.util.Date;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 11:23
 * @description 存储日期的线程本地内存
 */

public class DateThreadLocalUtils {

    // 线程变量隔离
    private DateThreadLocalUtils(){};

    private static final ThreadLocal<Date> TIME_THREAD_LOCAL = new ThreadLocal<>();

    public static void put(Date date){
        TIME_THREAD_LOCAL.set(date);
    }

    public static Date get(){
        return TIME_THREAD_LOCAL.get();
    }

    public static void remove(){
        TIME_THREAD_LOCAL.remove();
    }

}
