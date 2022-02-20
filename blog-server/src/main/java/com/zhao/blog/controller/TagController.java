package com.zhao.blog.controller;


import com.zhao.blog.common.annotations.MyCache;
import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.domain.entity.Tag;
import com.zhao.blog.service.ITagService;
import com.zhao.blog.vo.TagVo;
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
 * @since 2022-01-18
 */

@Slf4j
@RestController
@RequestMapping("/tag")
@Api(value = "标签管理", tags = "标签管理")
public class TagController {

    @Resource
    private ITagService tagService;

    @MyCache(name = "hot_tag")
    @MyLogger(module = "标签管理", operation = "查询最热6个标签")
    @ApiOperation(value = "查询最热6个标签")
    @GetMapping("/public/hot")
    public Response queryTheSixHotTag() {

        List<Tag> result = tagService.listTheSixHotTag();

        if (null == result) {
            log.info("[goo-blog|TagController|queryTheSixHotTag] 数据库暂无标签信息！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "暂无标签信息！", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询最热六个标签成功！", result);
    }

    @MyCache(name = "all_tag")
    @MyLogger(module = "标签管理", operation = "查询所有标签")
    @ApiOperation(value = "查询所有标签")
    @GetMapping("/public/all")
    public Response queryAllTag() {

        List<TagVo> result = tagService.listAllTag();

        if (null == result) {
            log.info("[goo-blog|TagController|queryAllTag] 数据库暂无标签信息！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "暂无标签信息！", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询所有标签信息成功！", result);
    }
}
