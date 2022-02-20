package com.zhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.blog.domain.dos.ArchivesDo;
import com.zhao.blog.domain.entity.Article;
import com.zhao.blog.vo.ArticleOptimizeVo;
import com.zhao.blog.vo.params.ArchivesParams;
import com.zhao.blog.vo.params.PageParams;
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
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询归档文章
     * @return List<ArchivesDo>
     * @param archivesParams
     */
    List<ArchivesDo> selectArchivesArticle(@Param("archivesParams") ArchivesParams archivesParams);

    /**
     * 查询文章
     * @return IPage<ArticleOptimizeDo>
     * @param pageParams
     */
    IPage<ArticleOptimizeVo> selectArticle(Page<ArticleOptimizeVo> page, @Param("pageParams") PageParams pageParams);

    /**
     * 查询文章详细信息
     * @param id
     * @return ArticleOptimizeVo
     */
    ArticleOptimizeVo selectArticleById(@Param("id") Long id);

    /**
     * 分页查询自己所有文章
     * @param articleOptimizeVoPage
     * @param pageParams
     * @param id
     * @return IPage<ArticleOptimizeVo>
     */
    IPage<ArticleOptimizeVo> selectMyselfArticle(Page<ArticleOptimizeVo> articleOptimizeVoPage
            , @Param("pageParams") PageParams pageParams
            , @Param("id") Long id);


    /**
     * 指定条件分页查询文章列表
     * @param articleOptimizeVoPage
     * @param pageParams
     * @return IPage<ArticleOptimizeVo>
     */
    IPage<ArticleOptimizeVo> selectArticleByTagId(Page<ArticleOptimizeVo> articleOptimizeVoPage, @Param("pageParams") PageParams pageParams);

    /**
     * 为es查询文章数据，首页显示类型
     * @return List<ArticleOptimizeVo>
     */
    List<ArticleOptimizeVo> selectArticleForEs();

}
