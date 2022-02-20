package com.zhao.blog.common.enums;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/19 11:34
 * @description 状态码
 */
public enum StatusCode {

    /** 客户端有关 **/
    STATUS_CODEC200(200, "对应请求成功"),
    STATUS_CODEC400(400, "对应非法参数"),
    STATUS_CODEC401(401, "对应未登录，请先登陆后再尝试"),
    STATUS_CODEC403(403, "对应权限不足，请联系管理员"),
    STATUS_CODEC404(404, "对应找不到页面"),
    /** 服务器有关 **/
    STATUS_CODEC500(500, "对应服务器内部错误"),
    STATUS_CODEC10001(10001, "对应结果为空"),
    STATUS_CODEC10002(10002, "对应必要参数为空"),
    STATUS_CODEC10003(10003, "对应sa-token有关"),
    STATUS_CODEC10004(10004, "服务器意外异常错误");

    /** 自定义状态码 **/
    private final Integer code;

    /** 自定义描述 **/
    private final String message;

    StatusCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
