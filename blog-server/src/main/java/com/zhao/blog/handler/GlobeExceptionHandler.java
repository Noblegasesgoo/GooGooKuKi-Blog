package com.zhao.blog.handler;

import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/19 20:57
 * @description 全局异常处理
 */

@ControllerAdvice
public class GlobeExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response doException(Exception e) {

        e.printStackTrace();
        return new Response(StatusCode.STATUS_CODEC10004.getCode(), "意料之外系统异常!", null);
    }
}
