package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.ArticleTag;
import com.zhao.blog.mapper.ArticleTagMapper;
import com.zhao.blog.service.IArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {


    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 根据tagid查询对应的文章id
     * @param tagId
     * @return List<Long>
     */
    @Override
    public List<Long> listArticleIdByTagId(Long tagId) {

        List<Long> articleIds = articleTagMapper.selectArticleIdByTagId(tagId);

        return articleIds;
    }
}
