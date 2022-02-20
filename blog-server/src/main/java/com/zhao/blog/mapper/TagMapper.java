package com.zhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhao.blog.domain.entity.Tag;
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
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询对应标签
     * @param articleId
     * @return List<Tags>
     */
    List<Tag> selectTagsByArticleId(Long articleId);

    /**
     * 查询最热6个标签
     * @return List<Tag>
     */
    List<Tag> selectTheSixHotTag();
}
