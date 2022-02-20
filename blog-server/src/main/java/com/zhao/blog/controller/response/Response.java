package com.zhao.blog.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/19 11:32
 * @description 响应封装类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /** 状态码 **/
    private Integer code;

    /** 提示信息 **/
    private String message;

    /** 响应数据 **/
    private Object data;
}
