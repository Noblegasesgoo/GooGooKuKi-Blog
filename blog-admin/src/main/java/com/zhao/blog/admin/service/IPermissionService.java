package com.zhao.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.admin.domain.params.PageParams;
import com.zhao.blog.admin.domain.entity.Permission;
import com.zhao.blog.admin.domain.params.PermissionParams;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface IPermissionService extends IService<Permission> {

    /**
     * 查询所有权限
     * @param pageParams
     * @return 权限和总数集
     */
    HashMap<String, Object> listAllPermission(PageParams pageParams);

    /**
     * 添加权限
     * @param permissionParams
     * @return 是否添加成功
     */
    Boolean addPermission(PermissionParams permissionParams);

    /**
     * 更新权限信息
     * @param permissionParams
     * @return 是否更新成功
     */
    Boolean updatePermission(PermissionParams permissionParams);

    /**
     * 禁用权限
     * @param id
     * @return 是否禁用成功
     */
    Boolean deletePermission(Long id);
}
