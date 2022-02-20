package com.zhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhao.blog.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Param;
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
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    /**
     * 根据tagid查询对应的文章id
     * @param tagId
     * @return List<Long>
     */
    List<Long> selectArticleIdByTagId(@Param("tagId") Long tagId);


}
