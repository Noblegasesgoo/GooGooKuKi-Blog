package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhao.blog.domain.dos.ArchivesDo;
import com.zhao.blog.domain.entity.Article;
import com.zhao.blog.domain.entity.ArticleBody;
import com.zhao.blog.domain.entity.ArticleTag;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.mapper.ArticleMapper;
import com.zhao.blog.service.*;
import com.zhao.blog.service.mq.BlogArticleSender;
import com.zhao.blog.utils.UserThreadLocalUtils;
import com.zhao.blog.vo.*;
import com.zhao.blog.vo.params.ArchivesParams;
import com.zhao.blog.vo.params.ArticleParams;
import com.zhao.blog.vo.params.EsParams;
import com.zhao.blog.vo.params.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.zhao.blog.common.consts.StaticData.NUMBER_ONE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */

@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ITagService tagService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IArticleBodyService articleBodyService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IThreadPoolForArticleService threadPoolForArticleService;

    @Autowired
    private IArticleTagService articleTagService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private BlogArticleSender blogArticleSender;


    /**
     * 指定条件分页查询文章列表
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    @Override
    public List<ArticleOptimizeVo> listArticleByTagId(PageParams pageParams) {
        log.debug("[goo-blog|ArticleServiceImpl|listArticleByTagId] 创建分页器");
        Page<ArticleOptimizeVo> articleOptimizeVoPage = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        log.debug("[goo-blog|ArticleServiceImpl|listArticleByTagId] 分页查询开始...");
        IPage<ArticleOptimizeVo> articleOptimizeDoIPage = articleMapper.selectArticleByTagId(articleOptimizeVoPage, pageParams);

        log.debug("[goo-blog|ArticleServiceImpl|listArticleByTagId] 查询完毕准备返回...");
        List<ArticleOptimizeVo> result = articleOptimizeDoIPage.getRecords();

        return result;
    }

    /**
     * 为Es查询数据
     * @return List<ArticleOptimizeVo>
     */
    @Override
    public List<ArticleOptimizeVo> listArticleForEs() {

        log.debug("[goo-blog|ArticleServiceImpl|listArticle] 查询开始...");
        List<ArticleOptimizeVo> result = articleMapper.selectArticleForEs();
        log.debug("[goo-blog|ArticleServiceImpl|listArticle] 查询完毕准备返回...");

        return result;
    }

    /**
     * 分页查询 满足条件的文章
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    @Override
    public List<ArticleOptimizeVo> listArticle(PageParams pageParams) {
        log.debug("[goo-blog|ArticleServiceImpl|listArticle] 创建分页器");
        Page<ArticleOptimizeVo> articleOptimizeVoPage = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        log.debug("[goo-blog|ArticleServiceImpl|listArticle] 分页查询开始...");
        IPage<ArticleOptimizeVo> articleOptimizeDoIPage = articleMapper.selectArticle(articleOptimizeVoPage, pageParams);

        log.debug("[goo-blog|ArticleServiceImpl|listArticle] 查询完毕准备返回...");
        List<ArticleOptimizeVo> result = articleOptimizeDoIPage.getRecords();

        return result;
    }

    /**
     * 分页查询 所有首页文章
     * @param pageParams
     * @return List<ArticleVo>
     */
    @Override
    public List<ArticleVo> listAllArticle(PageParams pageParams) {

        log.debug("[goo-blog|ArticleServiceImpl|listAllArticle] 创建分页器");
        /** 创建分页器 **/
        Page<Article> articlePage = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        log.debug("[goo-blog|ArticleServiceImpl|listAllArticle] 分页器创建完毕");

        log.debug("[goo-blog|ArticleServiceImpl|listAllArticle] 创建函数式条件查询器");
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper();

        /** 判断是否需要按类别分类 **/
        if (null != pageParams.getCategoryId()) {
            articleQueryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
        }

        ///** 判断是否需要按标签分类 **/
        //if (null != pageParams.getTagId()) {
        //    List<Long> articleTagDos = articleTagService.listArticleIdByTagId(pageParams.getTagId());
        //
        //    if (articleTagDos.size() > NUMBER_ZERO) {
        //        articleQueryWrapper.in(Article::getId, articleTagDos);
        //    }
        //}

        /** 根据是否置顶来排序 **/
        articleQueryWrapper.orderByDesc(Article::getIsTop,Article::getGmtCreate);
        log.debug("[goo-blog|ArticleServiceImpl|listAllArticle] 函数式条件查询器创建完毕");

        log.debug("[goo-blog|ArticleServiceImpl|listAllArticle] 分页查询开始");
        Page<Article> currentPage = articleMapper.selectPage(articlePage, articleQueryWrapper);
        log.debug("[goo-blog|ArticleServiceImpl|listAllArticle] 分页查询结束");

        List<Article> articleList = currentPage.getRecords();
        List<ArticleVo> result = copyList(articleList, true, true);

        return result;
    }

    /**
     * 查询指定数量最热文章
     * @return List<ArticleVo>
     */
    @Override
    public List<ArticleVo> listHotArticle(Integer count) {

        log.debug("[goo-blog|ArticleServiceImpl|listHotArticle] 添加查询条件中...");
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(Article::getId, Article::getTitle)
                .orderByDesc(Article::getViewCounts).last("limit " + count);
        log.debug("[goo-blog|ArticleServiceImpl|listHotArticle] 条件添加完成！");

        log.debug("[goo-blog|ArticleServiceImpl|listHotArticle] 查询开始...");
        List<Article> articles = articleMapper.selectList(articleLambdaQueryWrapper);
        log.debug("[goo-blog|ArticleServiceImpl|listHotArticle] 查询结束！");

        List<ArticleVo> result = copyList(articles, false, false);

        return result;
    }

    /**
     * 查询最新文章
     * @return List<ArticleVo>
     */
    @Override
    public List<ArticleVo> listNewArticle() {
        log.debug("[goo-blog|ArticleServiceImpl|listNewArticle] 添加查询条件中...");
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(Article::getId, Article::getTitle, Article::getGmtCreate)
                .orderByDesc(Article::getGmtCreate);
        log.debug("[goo-blog|ArticleServiceImpl|listNewArticle] 条件添加完成！");

        log.debug("[goo-blog|ArticleServiceImpl|listNewArticle] 查询开始...");
        List<Article> articles = articleMapper.selectList(articleLambdaQueryWrapper);
        log.debug("[goo-blog|ArticleServiceImpl|listNewArticle] 查询结束！");

        List<ArticleVo> result = copyList(articles, false, false);

        return result;
    }

    /**
     * 查询归档文章
     * @param archivesParams
     * @return List<ArchivesDo>
     */
    @Override
    public List<ArchivesDo> listArchivesArticle(ArchivesParams archivesParams) {

        List<ArchivesDo> archivesArticle =  articleMapper.selectArchivesArticle(archivesParams);
        return archivesArticle;
    }

    /**
     * 查询文章详情请求
     * @param id
     * @return ArticleVo
     */
    @Override
    public ArticleOptimizeVo listArticleById(Long id) {

        //Article article = articleMapper.selectById(id);
        //ArticleVo articleVo = copy(article, true, true, true, true);

        ArticleOptimizeVo articleVo = articleMapper.selectArticleById(id);

        Article article = new Article();
        article.setId(articleVo.getId());
        article.setViewCounts(articleVo.getViewCounts());

        /** 用线程池的多线程以及他的特点来进行浏览和评论的同步 **/
        /** 同时将此时的观看数量丢到 redis 中准备做定时任务，这样也可以保证原子性增长 **/
        threadPoolForArticleService.updateArticleViewCount(articleVo.getId());

        /** 如果缓存中的观看数不为空，那就用缓存的来代替数据库的，毕竟这边不会出错 **/
        String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(id));
        if (viewCount != null){
            articleVo.setViewCounts(Integer.parseInt(viewCount));
        }

        return articleVo;
    }

    /**
     * 文章发布
     * @param articleParams
     * @return ArticleVo
     */
    @Override
    @Transactional
    public ArticleVo publishArticleByCurrentUser(ArticleParams articleParams) {

        /** 从登陆过后的本地线程存储空间中拿到当前创建文章的用户的信息 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 从登陆过后的本地线程存储空间中拿到当前创建文章的用户的信息...");
        SysUser user = UserThreadLocalUtils.get();

        /** 初始化新的文章对象 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 初始化新的文章对象...");
        Article article = new Article();

        /** 设置文章基础信息，并且默认不置顶 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 设置文章基础信息，并且默认不置顶...");
        article.setAuthorId(user.getId());
        article.setTitle(articleParams.getTitle());
        article.setSummary(articleParams.getSummary());
        article.setCategoryId(articleParams.getCategory().getId());

        /** 先插入获取当前文章的id **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 先插入获取当前文章的id...");
        articleMapper.insert(article);

        /** 获取文章对应的标签并设置 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 获取文章对应的标签并设置...");
        List<TagVo> tags = articleParams.getTags();
        if (null != tags) {

            /** 设置文章对应标签 **/
            List<ArticleTag> articleTags = new ArrayList<>();
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();

                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());

                articleTags.add(articleTag);
            }

            /** 讲文章对应标签批量插入关联表**/
            log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 讲文章对应标签批量插入关联表...");
            articleTagService.saveBatch(articleTags);
        }

        /** 获取文章内容并设置 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 获取文章内容并设置...");
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        articleBodyService.save(articleBody);

        /** 设置文章与内容的关联 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 设置文章与内容的关联...");
        article.setBodyId(articleBody.getId());

        /** 更新文章信息 **/
        log.debug("[goo-blog|ArticleServiceImpl|publishArticleByCurrentUser] 更新文章信息...");
        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();


        articleVo.setId(article.getId());

        return articleVo;
    }

    /**
     * 条件查询对应用户所写文章请求
     * @param pageParams
     * @return List<ArticleOptimizeVo>
     */
    @Override
    public List<ArticleOptimizeVo> listMyselfArticle(PageParams pageParams) {
        log.debug("[goo-blog|ArticleServiceImpl|listMyselfArticle] 创建分页器");
        Page<ArticleOptimizeVo> articleOptimizeVoPage = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        log.debug("[goo-blog|ArticleServiceImpl|listMyselfArticle] 分页查询开始...");
        IPage<ArticleOptimizeVo> articleOptimizeDoIPage = articleMapper.selectMyselfArticle(articleOptimizeVoPage, pageParams, pageParams.getAuthorId());

        log.debug("[goo-blog|ArticleServiceImpl|listMyselfArticle] 查询完毕准备返回...");
        List<ArticleOptimizeVo> result = articleOptimizeDoIPage.getRecords();

        return result;
    }

    /**
     * 修改文章
     * @param articleParams
     * @return Boolean
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Boolean updateArticleByCurrentUser(ArticleParams articleParams) {

        /** 初始化新的文章对象 **/
        log.debug("[goo-blog|ArticleServiceImpl|updateArticleByCurrentUser] 初始化新的文章对象...");
        Article article = new Article();

        /** 设置文章基础信息，并且默认不置顶 **/
        log.debug("[goo-blog|ArticleServiceImpl|updateArticleByCurrentUser] 设置文章基础信息，并且默认不置顶...");
        article.setId(articleParams.getArticleId());
        article.setAuthorId(articleParams.getAuthorId());
        article.setTitle(articleParams.getTitle());
        article.setSummary(articleParams.getSummary());
        article.setCategoryId(articleParams.getCategory().getId());
        log.debug("[goo-blog|ArticleServiceImpl|updateArticleByCurrentUser] 更新文章信息...");

        /** 获取文章对应的标签并重新设置 **/
        log.debug("[goo-blog|ArticleServiceImpl|updateArticleByCurrentUser] 获取文章对应的标签并设置...");
        List<TagVo> tags = articleParams.getTags();

        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleParams.getArticleId());
        boolean remove = articleTagService.remove(articleTagLambdaQueryWrapper);
        /** 获得原来文章绑定的标签，将其删除 **/
        if (!remove) {

            return false;
        }

        if (null != tags) {

            /** 设置文章对应标签 **/
            List<ArticleTag> articleTags = new ArrayList<>();
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();

                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());

                articleTags.add(articleTag);
            }

            /** 讲文章对应标签批量插入关联表**/
            log.debug("[goo-blog|ArticleServiceImpl|updateArticleByCurrentUser] 讲文章对应标签批量插入关联表...");
            articleTagService.saveBatch(articleTags);
        }

        /** 获取文章内容并设置 **/
        log.debug("[goo-blog|ArticleServiceImpl|updateArticleByCurrentUser] 获取文章内容并设置...");
        ArticleBody articleBody = new ArticleBody();
        articleBody.setId(articleParams.getBody().getId());
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        articleBodyService.updateById(articleBody);

        /** 更新文章信息 **/
        int i = articleMapper.updateById(article);

        if (i == NUMBER_ONE) {

            /** 延迟至数据库更新之后再更新缓存 **/
            blogArticleSender.toDoDeleteArticleContentCache(article.getId());
            return true;
        }

        return false;
    }

    /**
     * copy列表的方法
     * @param articleList
     * @param hasTag
     * @param hasAuthor
     * @return List<ArticleVo>
     */
    private List<ArticleVo> copyList(List<Article> articleList
            , Boolean hasTag
            , Boolean hasAuthor) {
        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝List<ArticleVo>对象中...");
        List<ArticleVo> articleVoList = new ArrayList<>();
        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝ArticleVO对象中...");

        for (Article article : articleList) {

            articleVoList.add(copy(article, hasTag, hasAuthor));
        }
        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝ArticleVO对象完成！");

        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝List<ArticleVo>对象完成!");
        return articleVoList;
    }

    /**
     * copy列表的方法
     * @param articleList
     * @param hasTag
     * @param hasAuthor
     * @param hasBody
     * @param hasCategory
     * @return List<ArticleVo>
     */
    private List<ArticleVo> copyList(List<Article> articleList
            , Boolean hasTag
            , Boolean hasAuthor
            , Boolean hasBody
            , Boolean hasCategory) {
        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝List<ArticleVo>对象中...");
        List<ArticleVo> articleVoList = new ArrayList<>();
        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝ArticleVO对象中...");
        for (Article article : articleList) {

            articleVoList.add(copy(article, hasTag, hasAuthor, hasBody, hasCategory));
        }
        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝ArticleVO对象完成！");

        log.debug("[goo-blog|ArticleServiceImpl|copyList] 拷贝List<ArticleVo>对象完成!");
        return articleVoList;
    }

    /**
     * copy实例对象的方法
     * @param article
     * @param hasTag
     * @param hasAuthor
     * @return ArticleVo
     */
    private ArticleVo copy(Article article
            , Boolean hasTag
            , Boolean hasAuthor) {

        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        log.debug("[goo-blog|ArticleServiceImpl|copy] 检查是否需要标签...");
        if (hasTag) {
            log.debug("[goo-blog|ArticleServiceImpl|copy] 需要标签！");
            Long articleId = article.getId();
            articleVo.setTags(tagService.listTagsByArticleId(articleId));
            log.debug("[goo-blog|ArticleServiceImpl|copy] 标签设置成功！");
        }

        log.debug("[goo-blog|ArticleServiceImpl|copy] 检查是否需要作者信息...");
        if (hasAuthor) {
            log.debug("[goo-blog|ArticleServiceImpl|copy] 需要作者信息！");
            Long authorId = article.getAuthorId();
            SysUser user = sysUserService.selectUserByAuthorId(authorId);
            articleVo.setAuthorId(user.getId());
            articleVo.setAuthorName(user.getNickname());
            articleVo.setAuthorFace(user.getFace());
            log.debug("[goo-blog|ArticleServiceImpl|copy] 作者信息设置成功！");
        }

        return articleVo;
    }

    /**
     * copy实例对象的方法
     * @param article
     * @param hasTag
     * @param hasAuthor
     * @param hasBody
     * @param hasCategory
     * @return ArticleVo
     */
    private ArticleVo copy(Article article
            , Boolean hasTag
            , Boolean hasAuthor
            , Boolean hasBody
            , Boolean hasCategory) {

        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        log.debug("[goo-blog|ArticleServiceImpl|copy] 检查是否需要标签...");
        if (hasTag) {
            log.debug("[goo-blog|ArticleServiceImpl|copy] 需要标签！");
            Long articleId = article.getId();
            articleVo.setTags(tagService.listTagsByArticleId(articleId));
            log.debug("[goo-blog|ArticleServiceImpl|copy] 标签设置成功！");
        }

        log.debug("[goo-blog|ArticleServiceImpl|copy] 检查是否需要作者信息...");
        if (hasAuthor) {
            log.debug("[goo-blog|ArticleServiceImpl|copy] 需要作者信息！");
            Long authorId = article.getAuthorId();
            SysUser user = sysUserService.selectUserByAuthorId(authorId);
            articleVo.setAuthorId(user.getId());
            articleVo.setAuthorName(user.getNickname());
            articleVo.setAuthorFace(user.getFace());
            log.debug("[goo-blog|ArticleServiceImpl|copy] 作者信息设置成功！");
        }

        log.debug("[goo-blog|ArticleServiceImpl|copy] 检查是否需要内容详情...");
        if (hasBody) {
            log.debug("[goo-blog|ArticleServiceImpl|copy] 需要内容详情！");
            ArticleBodyVo articleBodyVo = articleBodyService.listArticleBodyById(article.getBodyId());
            articleVo.setBody(articleBodyVo);
            log.debug("[goo-blog|ArticleServiceImpl|copy] 内容详情设置成功！");
        }

        log.debug("[goo-blog|ArticleServiceImpl|copy] 检查是否需要文章分类...");
        if (hasCategory) {
            log.debug("[goo-blog|ArticleServiceImpl|copy] 需要文章分类！");

            CategoryVo categoryVo = categoryService.listCategoryById(article.getCategoryId());
            articleVo.setCategory(categoryVo);

            log.debug("[goo-blog|ArticleServiceImpl|copy] 文章分类设置成功！");
        }

        return articleVo;
    }


    /**
     * 根据文章id查询对应文章
     * @param articleId
     * @return Article
     */
    @Override
    public Article getAuthorIdByArticleId(Long articleId) {

        Article article = articleMapper.selectById(articleId);

        return article;
    }

    /**
     * 根据条件从Es中模糊搜索中文
     * @param esParams
     * @return List<ArticleOptimizeVo>
     */
    @Override
    public List<ArticleOptimizeVo> listArticleByFromEs(EsParams esParams){

        List<ArticleOptimizeVo> articleOptimizeVos = new LinkedList<>();

        /** 开始查询 **/
        try {
            /** 从ES查询数据 **/
            SearchRequest searchRequest = new SearchRequest("googookukiblogarticlesindex");

            /** 查询条件 **/
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(esParams.getCondition(), "title", "summary"));

            /** 分页条件 **/
            searchSourceBuilder.from((esParams.getPage()-1) * esParams.getPageSize());
            searchSourceBuilder.size(esParams.getPageSize());

            /** 高亮显示 **/
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            HighlightBuilder.Field field = new HighlightBuilder.Field("title");

            highlightBuilder.field(field);
            highlightBuilder.preTags("<label style='color:green'>");
            highlightBuilder.postTags("</lable>");

            searchSourceBuilder.highlighter(highlightBuilder);

            /** 所有条件设置完毕准备查询 **/
            searchRequest.source(searchSourceBuilder);

            /** 封装查询结果 **/
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();

            Iterator<SearchHit> iterator = hits.iterator();
            while (iterator.hasNext()) {

                SearchHit searchHit = iterator.next();
                ArticleOptimizeVo articleOptimizeVo = objectMapper.readValue(searchHit.getSourceAsString(), ArticleOptimizeVo.class);
                articleOptimizeVos.add(articleOptimizeVo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return articleOptimizeVos;
    }
}
