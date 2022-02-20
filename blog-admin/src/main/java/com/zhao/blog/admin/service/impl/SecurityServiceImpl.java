package com.zhao.blog.admin.service.impl;

import com.zhao.blog.admin.domain.entity.Admin;
import com.zhao.blog.admin.service.IAdminService;
import com.zhao.blog.admin.service.ISecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/7 15:10
 * @description
 */

@Slf4j
@Service
public class SecurityServiceImpl implements ISecurityService, UserDetailsService {

    @Autowired
    private IAdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {

        Admin admin = adminService.listUserByPhoneNumber(phoneNumber);

        /** 是否有该管理员 **/
        if (null == admin) {
            return null;
        }

        UserDetails userDetails = new User(phoneNumber, admin.getPassword(), new ArrayList<>());
        return userDetails;
    }
}
