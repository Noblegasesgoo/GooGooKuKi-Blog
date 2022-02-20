package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.Comment;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.vo.CommentVo;
import com.zhao.blog.vo.params.CommentParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 查询对应文章的所有一级评论
     * @param id
     * @return List<CommentVo>
     */
    List<CommentVo> listAllCommentsForCurrentArticleLevelOne(Long id);

    /**
     * 创建一条新评论|回复一条评论
     * @param commentParams
     * @param user
     * @return Boolean
     */
    Boolean createComment(CommentParams commentParams, SysUser user);

    /**
     * 查询对应文章的所有二级评论
     * @param id
     * @return List<CommentVo>
     */
    List<CommentVo> listAllCommentsForCurrentArticleLevelTwo(Long id);

    /**
     * 删除评论
     * @param commentParams
     * @return Boolean
     */
    Boolean deletedComment(CommentParams commentParams);
}
