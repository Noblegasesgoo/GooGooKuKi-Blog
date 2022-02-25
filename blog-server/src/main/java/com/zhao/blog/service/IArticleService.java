package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.dos.ArchivesDo;
import com.zhao.blog.domain.entity.Article;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.vo.ArticleOptimizeVo;
import com.zhao.blog.vo.ArticleVo;
import com.zhao.blog.vo.params.ArchivesParams;
import com.zhao.blog.vo.params.ArticleParams;
import com.zhao.blog.vo.params.EsParams;
import com.zhao.blog.vo.params.PageParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface IArticleService extends IService<Article> {

    /**
     * 为Es查询数据
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    List<ArticleOptimizeVo> listArticleForEs();

    /**
     * 分页查询 满足条件的文章
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    List<ArticleOptimizeVo> listArticle(PageParams pageParams);


    /**
     * 分页查询 所有首页文章
     * @param pageParams
     * @return List<ArticleVo>
     */
    List<ArticleVo> listAllArticle(PageParams pageParams);

    /**
     * 查询指定数量最热文章
     * @param count
     * @return List<ArticleVo>
     */
    List<ArticleVo> listHotArticle(Integer count);

    /**
     * 查询最新文章
     * @return List<ArticleVo>
     */
    List<ArticleVo> listNewArticle();

    /**
     * 查询归档文章
     * @param archivesParams
     * @return List<ArchivesDo>
     */
    List<ArchivesDo> listArchivesArticle(ArchivesParams archivesParams);

    /**
     * 查询文章详情
     * @param id
     * @return ArticleVo
     */
    ArticleOptimizeVo listArticleById(Long id);

    /**
     * 文章发布
     * @param archivesParams
     * @return ArticleVo
     */
    ArticleVo publishArticleByCurrentUser(ArticleParams archivesParams);

    /**
     * 条件查询对应用户所写文章
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    List<ArticleOptimizeVo> listMyselfArticle(PageParams pageParams);

    /**
     * 修改文章
     * @param articleParams
     * @return Boolean
     */
    Boolean updateArticleByCurrentUser(ArticleParams articleParams);

    /**
     * 指定条件分页查询文章列表
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    List<ArticleOptimizeVo> listArticleByTagId(PageParams pageParams);

    /**
     * 根据文章id查询对应文章
     * @param articleId
     * @return Article
     */
    Article getAuthorIdByArticleId(Long articleId);

    /**
     * 根据条件从Es中模糊搜索中文
     * @param esParams
     * @return List<ArticleOptimizeVo>
     */
    List<ArticleOptimizeVo> listArticleByFromEs(EsParams esParams);

    /**
     * 删除文章（逻辑删除）
     * @param user
     * @param articleParams
     * @return Boolean
     */
    Boolean deleteArticleByAuthor(SysUser user, ArticleParams articleParams);
}
