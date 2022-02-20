package com.zhao.blog.controller;


import com.zhao.blog.common.annotations.MyCache;
import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.service.ICategoryService;
import com.zhao.blog.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-21
 */

@Slf4j
@RestController
@RequestMapping("/category")
@Api(value = "类别管理接口", tags = "类别管理")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    @MyCache(name = "all_category")
    @MyLogger(module = "类别管理", operation = "查询所有分类信息请求")
    @GetMapping("/public/all")
    @ApiOperation(value = "查询所有分类信息请求")
    public Response queryAllCategory() {

        List<CategoryVo> result = categoryService.listAllCategory();

        if (null == result) {
            log.info("[goo-blog|CategoryController|queryAllCategory] 数据库没有分类信息！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "暂无分类信息！", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询所有分类信息成功！", result);
    }


}
