package com.zhao.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.admin.domain.params.PageParams;
import com.zhao.blog.admin.domain.entity.Permission;
import com.zhao.blog.admin.domain.params.PermissionParams;
import com.zhao.blog.admin.mapper.PermissionMapper;
import com.zhao.blog.admin.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

import static com.zhao.blog.admin.common.consts.StaticData.NUMBER_ZERO;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {


    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 查询所有权限
     * @param pageParams
     * @return 权限和总数集
     */
    @Override
    public HashMap<String, Object> listAllPermission(PageParams pageParams) {

        Page<Permission> permissionVoPage = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Permission> permissionLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(pageParams.getPermissionName())) {
            permissionLambdaQueryWrapper.eq(Permission::getPermissionName, pageParams.getPermissionName());
        }

        Page<Permission> permissionPage = permissionMapper.selectPage(permissionVoPage, permissionLambdaQueryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", permissionPage.getTotal());
        map.put("records", permissionPage.getRecords());

        return map;
    }

    /**
     * 添加权限
     * @param permissionParams
     * @return 是否添加成功
     */
    @Override
    public Boolean addPermission(PermissionParams permissionParams) {

        LambdaQueryWrapper<Permission> permissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        permissionLambdaQueryWrapper.eq(Permission::getPermissionName, permissionParams.getPermissionName())
                .eq(Permission::getDescription, permissionParams.getDescription())
                .eq(Permission::getPath, permissionParams.getPath());

        /** 查询有无对应存在的权限 **/
        Permission permission = permissionMapper.selectOne(permissionLambdaQueryWrapper);
        if (null != permission) {
            return false;
        }

        /** 执行添加操作 **/
        permission.setDescription(permissionParams.getDescription())
                .setPermissionName(permissionParams.getPermissionName())
                .setPath(permissionParams.getPath());
        int result = permissionMapper.insert(permission);

        if (result == NUMBER_ZERO) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 更新权限信息
     *
     * @param permissionParams
     * @return 是否更新成功
     */
    @Override
    public Boolean updatePermission(PermissionParams permissionParams) {

        Permission permission = new Permission();
        permission.setId(permissionParams.getId())
                .setPermissionName(permissionParams.getPermissionName())
                .setDescription(permissionParams.getDescription())
                .setPath(permissionParams.getPath())
                .setGmtModified(new Date());

        int result = permissionMapper.updateById(permission);

        if (result == NUMBER_ZERO) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 禁用权限
     * @param id
     * @return 是否禁用成功
     */
    @Override
    public Boolean deletePermission(Long id) {

        Permission permission = new Permission();
        permission.setId(id);
        permission.setIsDeleted(true);

        int result = permissionMapper.updateById(permission);

        if (result == NUMBER_ZERO) {
            return false;
        } else {
            return true;
        }

    }

}
