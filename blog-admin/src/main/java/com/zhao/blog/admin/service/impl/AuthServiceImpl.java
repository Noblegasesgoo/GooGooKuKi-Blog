package com.zhao.blog.admin.service.impl;

import com.zhao.blog.admin.service.IAuthService;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/7 17:53
 * @description 权限服务实现类
 */
public class AuthServiceImpl implements IAuthService {

    /**
     * 权限获取
     * @param request
     * @param authorityList
     * @return 是否获取成功
     */
    @Override
    public Boolean auth(HttpServletRequest request, List<GrantedAuthority> authorityList) {
        return true;
    }
}
