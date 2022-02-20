package com.zhao.blog.utils;

import com.zhao.blog.domain.entity.SysUser;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/21 13:23
 * @description ThreadLocal工具类
 */
public class UserThreadLocalUtils {

    private UserThreadLocalUtils(){}

    // 线程变量隔离
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
