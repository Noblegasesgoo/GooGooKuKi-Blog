package com.zhao.blog.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 10:49
 * @description HttpContext请求上下文工具类
 */

public class HttpContextUtils {

    /**
     * 获取 HttpServletRequest 对象
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
