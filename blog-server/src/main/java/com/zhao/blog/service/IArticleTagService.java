package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.ArticleTag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface IArticleTagService extends IService<ArticleTag> {

    /**
     * 根据tagid查询对应的文章id
     * @param tagId
     * @return List<Long>
     */
    List<Long> listArticleIdByTagId(Long tagId);
}
