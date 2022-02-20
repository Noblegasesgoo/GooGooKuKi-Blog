package com.zhao.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.admin.domain.entity.Admin;
import com.zhao.blog.admin.mapper.AdminMapper;
import com.zhao.blog.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-02-07
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 通过手机号查找管理员
     * @param phoneNumber
     * @return 管理员
     */
    @Override
    public Admin listUserByPhoneNumber(String phoneNumber) {

        LambdaQueryWrapper<Admin> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(Admin::getUsername, phoneNumber)
                .last("LIMIT 1");

        Admin admin = adminMapper.selectOne(sysUserLambdaQueryWrapper);

        return admin;
    }

}
