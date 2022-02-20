package com.zhao.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.admin.domain.entity.Admin;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-02-07
 */

public interface IAdminService extends IService<Admin> {

    /**
     * 通过手机号查找管理员
     * @param phoneNumber
     * @return 管理员
     */
    Admin listUserByPhoneNumber(String phoneNumber);
}
