package com.zhao.blog.service;

import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.vo.params.LoginParams;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/20 14:02
 * @description 登陆服务
 */

public interface ILoginService {

    /**
     * 登陆并返回token
     * @param loginParams
     * @return token
     */
    String login(LoginParams loginParams);

    /**
     * 注销
     * @param token
     * @return Boolean
     */
    Boolean logout(String token);

    /**
     * 查找token对应的用户信息（检查token合法性）
     * @param token
     * @return SysUser
     */
    SysUser checkToken(String token);
}
