package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.ArticleBody;
import com.zhao.blog.vo.ArticleBodyVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface IArticleBodyService extends IService<ArticleBody> {

    /**
     * 通过id查询对应文章内容
     * @param id
     * @return ArticleBodyVo
     */
    ArticleBodyVo listArticleBodyById(Long id);
}
