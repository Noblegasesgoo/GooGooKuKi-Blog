package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.ArticleBody;
import com.zhao.blog.mapper.ArticleBodyMapper;
import com.zhao.blog.service.IArticleBodyService;
import com.zhao.blog.vo.ArticleBodyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ArticleBodyServiceImpl extends ServiceImpl<ArticleBodyMapper, ArticleBody> implements IArticleBodyService {

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    /**
     * 通过id查询对应文章内容
     * @param id
     * @return ArticleBodyVo
     */
    @Override
    public ArticleBodyVo listArticleBodyById(Long id) {

        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] 根据id查询对应文章内容...");
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] 查询结束！");

        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] 设置 ArticleBodyVo 属性...");
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setId(articleBody.getId());
        articleBodyVo.setContent(articleBody.getContent());
        articleBodyVo.setContentHtml(articleBody.getContentHtml());
        log.info("[goo-blog|SysUserServiceImpl|selectUserByAuthorId] ArticleBodyVo 属性设置完成！正在返回...");

        return articleBodyVo;
    }
}
