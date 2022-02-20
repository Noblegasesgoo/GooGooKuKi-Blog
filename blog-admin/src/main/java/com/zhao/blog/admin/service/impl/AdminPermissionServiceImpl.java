package com.zhao.blog.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.admin.domain.entity.AdminPermission;
import com.zhao.blog.admin.mapper.AdminPermissionMapper;
import com.zhao.blog.admin.service.IAdminPermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
@Service
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermission> implements IAdminPermissionService {

}
