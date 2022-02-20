package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.Tag;
import com.zhao.blog.mapper.TagMapper;
import com.zhao.blog.service.ITagService;
import com.zhao.blog.vo.TagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 查找对应文章的标签信息
     * @param articleId
     * @return List<TagVo>
     */
    @Override
    public List<TagVo> listTagsByArticleId(Long articleId) {

        log.info("[goo-blog|TagServiceImpl|listTagsByArticleId] 开始查找对应文章id的标签...");
        List<Tag> tags = tagMapper.selectTagsByArticleId(articleId);
        log.info("[goo-blog|TagServiceImpl|listTagsByArticleId] 查找结束！");

        List<TagVo> result = copyList(tags);

        return result;
    }

    private List<TagVo> copyList(List<Tag> tags) {

        log.debug("[goo-blog|TagServiceImpl|copyList] 拷贝List<TagVo>对象中...");
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {

            tagVos.add(copy(tag));
        }
        log.debug("[goo-blog|TagServiceImpl|copyList] 拷贝List<TagVo>对象完成!");
        return tagVos;
    }

    private TagVo copy(Tag tag) {

        log.debug("[goo-blog|TagServiceImpl|copy] 拷贝TagVO对象中...");
        TagVo tagVO = new TagVo();
        BeanUtils.copyProperties(tag, tagVO);
        log.debug("[goo-blog|TagServiceImpl|copy] 拷贝TagVO对象完成！");

        return tagVO;
    }

    /**
     * 查询最热6个标签
     * @return List<Tag>
     */
    @Override
    public List<Tag> listTheSixHotTag() {

        log.debug("[goo-blog|TagServiceImpl|listTheSixHotTag] 查询最热6个标签中...");
        List<Tag> tags = tagMapper.selectTheSixHotTag();
        log.debug("[goo-blog|TagServiceImpl|listTheSixHotTag] 查询成功...");

        return tags;
    }

    /**
     * 查询所有标签
     *
     * @return List<TagVo>
     */
    @Override
    public List<TagVo> listAllTag() {

        log.info("[goo-blog|TagServiceImpl|listAllTag] 查询所有标签中...");
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<>());

        log.info("[goo-blog|TagServiceImpl|listAllTag] 设置vo对象中...");
        List<TagVo> result = copyList(tags);

        log.info("[goo-blog|TagServiceImpl|listAllTag] 设置完成准备返回...");
        return result;
    }
}
