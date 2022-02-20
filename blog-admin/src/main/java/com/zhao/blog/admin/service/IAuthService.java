package com.zhao.blog.admin.service;

import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/7 17:53
 * @description 权限服务
 */

public interface IAuthService {

    /**
     * 权限获取
     * @param request
     * @param authorityList
     * @return 是否获取成功
     */
    Boolean auth(HttpServletRequest request, List<GrantedAuthority> authorityList);

}
