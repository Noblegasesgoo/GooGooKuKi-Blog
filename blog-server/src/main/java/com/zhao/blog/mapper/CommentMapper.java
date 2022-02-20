package com.zhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhao.blog.domain.entity.Comment;
import com.zhao.blog.vo.CommentVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询对应文章的所有一级评论信息
     * @param id
     * @return List<CommentVo>
     */
    List<CommentVo> selectAllCommentsForCurrentArticleLevelOne(Long id);

    /**
     * 查询对应文章的所有二级评论信息
     * @param id
     * @return List<CommentVo>
     */
    List<CommentVo> selectAllCommentsForCurrentArticleLevelTow(Long id);
}
