package com.zhao.blog.controller;


import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.ICommentService;
import com.zhao.blog.utils.UserThreadLocalUtils;
import com.zhao.blog.vo.CommentVo;
import com.zhao.blog.vo.params.CommentParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/comment")
@Api(value = "评论管理接口", tags = "评论管理")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @MyLogger(module = "评论管理", operation = "查询对应文章的所有一级评论请求")
    @GetMapping("/public/article/level1/all/{id}")
    @ApiOperation(value = "查询对应文章的所有一级评论请求")
    public Response queryAllCommentsForCurrentArticleLevelOne(@PathVariable("id") Long id) {

        List<CommentVo> commentVos = commentService.listAllCommentsForCurrentArticleLevelOne(id);

        if (null == commentVos) {
            log.error("[goo-blog|CommentController|queryAllCommentsForCurrentArticle] 该文章没有评论！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "该文章没有评论！", commentVos);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询对应文章的所有一级评论成功！", commentVos);
    }

    @MyLogger(module = "评论管理", operation = "查询对应文章的所有二级评论请求")
    @GetMapping("/public/article/level2/all/{id}")
    @ApiOperation(value = "查询对应文章的所有二级评论请求")
    public Response queryAllCommentsForCurrentArticleLevelTwo(@PathVariable("id") Long id) {

        List<CommentVo> commentVos = commentService.listAllCommentsForCurrentArticleLevelTwo(id);

        if (null == commentVos) {
            log.info("[goo-blog|CommentController|queryAllCommentsForCurrentArticle] 该文章没有二级评论！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "该文章没有二级评论！", commentVos);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询对应文章的所有二级评论成功！", commentVos);
    }

    @MyLogger(module = "评论管理", operation = "新建|回复评论请求")
    @PostMapping("/private/create")
    @ApiOperation(value = "新建|回复评论请求")
    public Response createComment(CommentParams commentParams) {

        if (null == commentParams || null == commentParams.getLevel()) {
            log.error("[goo-blog|CommentController|createComment] 参数不合法，可能为空！请检查！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "参数不合法！", commentParams);
        }

        /** 从主线程本地缓存中拿出当前登陆的用户信息 **/
        SysUser user = UserThreadLocalUtils.get();
        Boolean comment = commentService.createComment(commentParams, user);
        if (!comment) {
            log.error("[goo-blog|CommentController|createComment] 数据库繁忙请修复！");
            return new Response(StatusCode.STATUS_CODEC500.getCode(), "数据库繁忙请稍后再试！", commentParams);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "评论成功！", commentParams);
    }


    @MyLogger(module = "评论管理", operation = "删除评论请求")
    @PostMapping("/private/delete")
    @ApiOperation(value = "删除评论请求")
    public Response deletedComment(CommentParams commentParams) {

        if (null == commentParams) {
            log.error("[goo-blog|CommentController|createComment] 参数不合法，可能为空！请检查！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "参数不合法！", commentParams);
        }

        /** 从主线程本地缓存中拿出当前登陆的用户信息 **/
        SysUser user = UserThreadLocalUtils.get();
        if (commentParams.getFromUserId() != user.getId()) {
            log.error("[goo-blog|CommentController|createComment] 用户没有删除该评论的权限！");
            return new Response(StatusCode.STATUS_CODEC403.getCode(), "您没有删除该评论的权限", null);
        }

        Boolean comment = commentService.deletedComment(commentParams);

        if (!comment) {
            log.error("[goo-blog|CommentController|createComment] 数据库繁忙请修复！");
            return new Response(StatusCode.STATUS_CODEC500.getCode(), "数据库繁忙请稍后再试！", commentParams);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "评论删除成功！", commentParams);
    }

}
