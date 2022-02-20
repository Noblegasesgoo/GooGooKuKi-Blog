package com.zhao.blog.admin.controller;


import com.zhao.blog.admin.common.enums.StatusCode;
import com.zhao.blog.admin.controller.response.Response;
import com.zhao.blog.admin.domain.params.PageParams;
import com.zhao.blog.admin.domain.params.PermissionParams;
import com.zhao.blog.admin.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

import static com.zhao.blog.admin.common.consts.StaticData.NUMBER_ZERO;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */

@Slf4j
@RestController
@RequestMapping("/admin/permission")
@Api(value = "权限管理", tags = "权限管理")
public class PermissionController {

    @Resource
    private IPermissionService permissionService;

    @ApiOperation(value = "查询权限列表请求")
    @PostMapping("/private/list")
    public Response queryPermissionList(@RequestBody PageParams pageParams) {

        if (null == pageParams.getPage() || null == pageParams.getPageSize()) {
            log.debug("[goo-blog|PermissionController|queryPermissionList] 页数或页面大小是非法参数！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "传入页数或页面大小非法！", null);
        }

        if (pageParams.getPage() <= NUMBER_ZERO || pageParams.getPageSize() <= NUMBER_ZERO) {
            log.error("[goo-blog|ArticleController|queryAllArticle] 页数或页面大小是非法参数！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "传入页数或页面大小非法！", null);
        }

        HashMap<String, Object> map = permissionService.listAllPermission(pageParams);

        if (null == map) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询失败！请稍后再试！", map);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询权限列表成功！", map);
    }

    @ApiOperation(value = "添加权限请求")
    @PostMapping("/private/add")
    public Response addPermission(@RequestBody PermissionParams permissionParams) {

        if (null == permissionParams.getPermissionName()
                || null == permissionParams.getDescription()
                || null == permissionParams.getPath()) {
            log.debug("[goo-blog|PermissionController|addPermission] 缺少必要参数！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", permissionParams);
        }

        Boolean result = permissionService.addPermission(permissionParams);

        if (!result) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "权限已存在或数据库错误！请稍后再试", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "添加权限成功！", result);
    }

    @ApiOperation(value = "更新权限请求")
    @PutMapping("/private/update")
    public Response updatePermission(@RequestBody PermissionParams permissionParams) {

        if (null != permissionParams.getId()) {
            log.debug("[goo-blog|PermissionController|addPermission] 缺少必要参数！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", permissionParams);
        }

        Boolean result = permissionService.updatePermission(permissionParams);

        if (!result) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "数据库错误，修改失败！请稍后再试", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "修改权限信息成功！", result);
    }


    @ApiOperation(value = "禁用权限请求")
    @DeleteMapping("/private/delete/{id}")
    public Response deletePermission(@PathVariable("id") Long id) {

        if (id<= NUMBER_ZERO || null == id) {
            log.error("[goo-blog|PermissionController|deletePermission] 页数或页面大小是非法参数！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "传入页数或页面大小非法！", null);
        }

        Boolean result = permissionService.deletePermission(id);

        if (!result) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "数据库错误，禁用失败！请稍后再试", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "禁用权限信息成功！", result);
    }

}
