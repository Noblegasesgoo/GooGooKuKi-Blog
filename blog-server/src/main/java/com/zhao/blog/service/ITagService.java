package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.Tag;
import com.zhao.blog.vo.TagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface ITagService extends IService<Tag> {

    /**
     * 查找对应文章的标签信息
     * @param articleId
     * @return List<TagVo>
     */
    List<TagVo> listTagsByArticleId(Long articleId);

    /**
     * 查询最热6个标签
     * @return List<Tag>
     */
    List<Tag> listTheSixHotTag();

    /**
     * 查询所有标签
     * @return List<TagVo>
     */
    List<TagVo> listAllTag();

}
