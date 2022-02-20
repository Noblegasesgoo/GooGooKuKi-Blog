package com.zhao.blog.handler;

import com.alibaba.fastjson.JSON;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.ILoginService;
import com.zhao.blog.utils.DateThreadLocalUtils;
import com.zhao.blog.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/21 12:06
 * @description 登陆拦截器
 */

@Slf4j
@Component
public class WebInterceptor implements HandlerInterceptor {

    @Autowired
    private ILoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /** 放行controller无关的请求 **/
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        /** 检查token是否为空 **/
        if (StringUtils.isBlank(token)) {

            Response result = new Response(StatusCode.STATUS_CODEC401.getCode(), "未登录，请先登录！", null);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));

            return false;
        }

        SysUser user = loginService.checkToken(token);

        /** 进一步检查token是否过期 **/
        if (null == user) {

            Response result = new Response(StatusCode.STATUS_CODEC401.getCode(), "登陆过期，请重新登陆！", null);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));

            return false;
        }

        UserThreadLocalUtils.put(user);

        /** token合法 **/
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        UserThreadLocalUtils.remove();
        DateThreadLocalUtils.remove();
    }

}
