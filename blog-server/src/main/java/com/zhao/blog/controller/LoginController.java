package com.zhao.blog.controller;

import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.service.ILoginService;
import com.zhao.blog.vo.params.LoginParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/20 13:59
 * @description
 */

@Slf4j
@RestController
@Api(value = "提供用户登录接口", tags = "登录管理")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @MyLogger(module = "登录管理", operation = "登陆")
    @PostMapping("/public/login")
    @ApiOperation(value = "登陆")
    public Response login(@RequestBody LoginParams loginParams) {

        if (null == loginParams) {
            log.error("[goo-blog|LoginController|login] 参数全为空：" + loginParams);
            return new Response(StatusCode.STATUS_CODEC400.getCode(), StatusCode.STATUS_CODEC10002.getMessage(), loginParams);
        }

        if (loginParams.getEmail().equals("")
                && loginParams.getPhoneNumber().equals("")
                && loginParams.getAccount().equals("")) {
            log.error("[goo-blog|LoginServiceImpl|login] 未填写用户名！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "未填写用户名！", null);
        }

        String token = loginService.login(loginParams);

        if (null == token) {
            log.error("[goo-blog|LoginController|login] 登陆失败，未查找到该用户！" + token);
            return new Response(StatusCode.STATUS_CODEC10001.getCode(), "登陆失败，没有此用户，或密码错误！", token);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "登陆成功！", token);
    }

    @MyLogger(module = "登录管理", operation = "注销")
    @PostMapping("/public/logout")
    @ApiOperation(value = "注销")
    public Response logout(@RequestHeader("Authorization") String token) {

        if (null == token && token.equals("")) {
            log.error("[goo-blog|LoginController|logout] 用户未登录！" + token);
            return new Response(StatusCode.STATUS_CODEC401.getCode(), "请先登录！", token);
        }

        Boolean result = loginService.logout(token);

        if (!result) {
            log.error("[goo-blog|LoginController|logout] 服务器内部错误，注销失败！" + token);
            return new Response(StatusCode.STATUS_CODEC10001.getCode(), "服务器内部错误，注销失败！", token);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "注销成功！正在跳转...！", result);
    }
}
