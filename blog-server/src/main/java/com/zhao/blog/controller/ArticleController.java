package com.zhao.blog.controller;


import com.zhao.blog.common.annotations.MyCache;
import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.domain.dos.ArchivesDo;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.IArticleService;
import com.zhao.blog.utils.UserThreadLocalUtils;
import com.zhao.blog.vo.ArticleOptimizeVo;
import com.zhao.blog.vo.ArticleVo;
import com.zhao.blog.vo.params.ArchivesParams;
import com.zhao.blog.vo.params.ArticleParams;
import com.zhao.blog.vo.params.EsParams;
import com.zhao.blog.vo.params.PageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.zhao.blog.common.consts.StaticData.NUMBER_ONE;
import static com.zhao.blog.common.consts.StaticData.NUMBER_ZERO;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */

@Slf4j
@RestController
@Api(value = "文章管理", tags = "文章管理")
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private IArticleService articleService;

    @MyLogger(module = "文章管理", operation = "指定条件分页查询首页文章列表请求")
    @ApiOperation(value = "指定条件分页查询首页文章列表请求")
    @PostMapping("/public/all/new")
    public Response queryArticleList(@RequestBody PageParams pageParams) {

        List<ArticleOptimizeVo> articleOptimizeVos = articleService.listArticle(pageParams);

        if (null == articleOptimizeVos) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "该条件下暂无文章！", articleOptimizeVos);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "文章查询成功！", articleOptimizeVos);
    }

    @MyLogger(module = "文章管理", operation = "分页查询首页文章列表请求")
    @ApiOperation(value = "分页查询首页文章列表请求")
    @PostMapping("/public/all/old")
    public Response queryAllArticle(@RequestBody PageParams pageParams){

        if (pageParams.getPage() < NUMBER_ZERO || pageParams.getPageSize() < NUMBER_ZERO) {
            log.error("[goo-blog|ArticleController|queryAllArticle] 页数或页面大小是非法参数！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "传入页数或页面大小非法！", null);
        }

        List<ArticleVo> articleVoList = articleService.listAllArticle(pageParams);

        if (null == articleVoList) {
            log.error("[goo-blog|ArticleController|queryAllArticle] 查询结果为空，请检查数据库！");
            return new Response(StatusCode.STATUS_CODEC10001.getCode(), "查询结果为空！", null);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询成功！", articleVoList);
    }

    @MyCache(name = "hot_article", expire = 24 * 60 * 1000 * 60)
    @MyLogger(module = "文章管理", operation = "查询指定数量最热文章请求")
    @ApiOperation(value = "查询指定数量最热文章请求")
    @GetMapping("/public/hot/{count}")
    public Response queryHotArticle(@PathVariable(value = "count") Integer count) {

        if (count < NUMBER_ONE) {
            log.error("[goo-blog|ArticleController|queryHotArticle] 传入总数为0，参数错误！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "传入要显示文章数量不能为空", null);
        }

        List<ArticleVo> result = articleService.listHotArticle(count);
        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询最热文章成功！", result);
    }

    @MyCache(name = "new_article", expire = 60 * 1000 * 60)
    @MyLogger(module = "文章管理", operation = "查询首页最新文章请求")
    @ApiOperation(value = "查询首页最新文章请求")
    @GetMapping("/public/new")
    public Response queryNewArticle() {

        List<ArticleVo> result = articleService.listNewArticle();
        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询最新文章成功！", result);
    }

    @MyCache(name = "archives_article")
    @MyLogger(module = "文章管理", operation = "查询归档文章请求")
    @ApiOperation(value = "查询归档文章请求")
    @PostMapping("/public/archives")
    public Response queryArchivesArticle(ArchivesParams archivesParams) {

        List<ArchivesDo> archivesArticle = articleService.listArchivesArticle(archivesParams);

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询文章归档成功！", archivesArticle);
    }

    @MyCache(name = "article_content")
    @MyLogger(module = "文章管理", operation = "查询文章详情请求")
    @ApiOperation(value = "查询文章详情请求")
    @PostMapping("/public/content/{id}")
    public Response queryArticleById(@PathVariable("id") Long id) {

        ArticleOptimizeVo article = articleService.listArticleById(id);
        if (null == article) {
            log.error("[goo-blog|ArticleController|queryArticleById] 查询结果为空，请检查数据库！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "查询结果为空，请检查数据库！", article);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询文章详情成功！", article);
    }

    @MyLogger(module = "文章管理", operation = "发布文章请求")
    @ApiOperation(value = "发布文章请求")
    @PostMapping("/private/publish")
    public Response publishArticleByCurrentUser(@RequestBody ArticleParams articleParams) {

        ArticleVo articleVo = articleService.publishArticleByCurrentUser(articleParams);

        if (NUMBER_ZERO == articleVo.getId()) {
            log.error("[goo-blog|ArticleController|publishArticleByCurrentUser] 文章插入失败！");
            return new Response(StatusCode.STATUS_CODEC500.getCode(), "发布文章失败！请稍后再试", articleVo);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "文章发布成功！", articleVo.getId());
    }

    @MyLogger(module = "文章管理", operation = "文章编辑请求")
    @ApiOperation(value = "文章编辑请求")
    @PutMapping("/private/update")
    public Response updateArticleByCurrentUser(@RequestBody ArticleParams articleParams) {

        SysUser user = UserThreadLocalUtils.get();
        if (articleParams.getAuthorId() != user.getId()) {
            log.error("[goo-blog|ArticleController|updateArticleByCurrentUser] 用户没有编辑该文章的权限！");
            return new Response(StatusCode.STATUS_CODEC403.getCode(), "您没有编辑该文章的权限", null);
        }

        Boolean result = articleService.updateArticleByCurrentUser(articleParams);

        if (!result) {
            log.error("[goo-blog|ArticleController|updateArticleByCurrentUser] 文章编辑失败！");
            return new Response(StatusCode.STATUS_CODEC500.getCode(), "发布编辑失败！请稍后再试", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "文章编辑成功！", result);
    }

    @MyLogger(module = "文章管理", operation = "条件查询对应用户所写文章请求")
    @ApiOperation(value = "条件查询对应用户所写文章请求")
    @PostMapping("/public/someone")
    public Response queryMyselfArticle(@RequestBody PageParams pageParams) {

        if (null == pageParams.getAuthorId() || null == pageParams.getPage() || null == pageParams.getPageSize()) {
            log.error("[blog|ArticleController|queryMyselfArticle] 参数不完整！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", pageParams);
        }

        List<ArticleOptimizeVo> articleOptimizeVos = articleService.listMyselfArticle(pageParams);

        if (null == articleOptimizeVos) {
            log.error("[goo-blog|ArticleController|queryMyselfArticle] 该用户未发表过文章！");
            return new Response(StatusCode.STATUS_CODEC500.getCode(), "暂无文章", articleOptimizeVos);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询个人文章成功！", articleOptimizeVos);
    }

    @MyLogger(module = "文章管理", operation = "指定标签分页查询首页文章列表请求")
    @ApiOperation(value = "指定标签分页查询首页文章列表请求")
    @PostMapping("/public/all/by/tag")
    public Response queryArticleListByTagId(@RequestBody PageParams pageParams) {

        if (null == pageParams.getTagId() || null == pageParams.getPage() || null == pageParams.getPageSize()) {
            log.error("[blog|ArticleController|queryArticleListByTagId] 参数不完整！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", pageParams);
        }

        List<ArticleOptimizeVo> articleOptimizeVos = articleService.listArticleByTagId(pageParams);

        if (null == articleOptimizeVos) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "该标签下暂无文章！", articleOptimizeVos);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "文章查询成功！", articleOptimizeVos);
    }

    @MyLogger(module = "文章管理", operation = "中文模糊查询接口")
    @ApiOperation(value = "中文模糊查询接口")
    @PostMapping("/public/all/es")
    public Response queryArticleListFromEs(@RequestBody EsParams esParams) {

        if (null == esParams.getCondition() || null == esParams.getPage() || null == esParams.getPageSize()) {
            log.error("[blog|ArticleController|queryArticleListFromEs] 参数不完整！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", esParams);
        }

        List<ArticleOptimizeVo> articleOptimizeVos = articleService.listArticleByFromEs(esParams);

        if (null == articleOptimizeVos) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "暂无文章！", articleOptimizeVos);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "文章查询成功！", articleOptimizeVos);
    }

    @MyLogger(module = "文章管理", operation = "逻辑删除文章接口")
    @ApiOperation(value = "逻辑删除文章接口")
    @PutMapping("/private/delete")
    public Response deleteArticleByAuthor(@RequestBody ArticleParams articleParams) {

        if (null == articleParams
                || null == articleParams.getArticleId()
                || null == articleParams.getAuthorId()) {
            log.error("[blog|ArticleController|deleteArticleByAuthor] 参数不完整！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", articleParams);
        }
        SysUser user = UserThreadLocalUtils.get();
        Boolean result = articleService.deleteArticleByAuthor(user, articleParams);

        if (!result) {
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "数据库错误，逻辑删除失败！", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "文章删除成功！", result);
    }
}
