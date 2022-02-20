package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.Category;
import com.zhao.blog.mapper.CategoryMapper;
import com.zhao.blog.service.ICategoryService;
import com.zhao.blog.vo.CategoryVo;
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
 * @since 2022-01-21
 */

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id查询对应的文章分类
     * @param categoryId
     * @return CategoryVo
     */
    @Override
    public CategoryVo listCategoryById(Long categoryId) {

        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] 根据id查询对应的文章分类...");
        Category category = categoryMapper.selectById(categoryId);
        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] 查询结束！");

        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] 设置 CategoryVo 属性...");
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(category.getId());
        categoryVo.setAvatar(category.getAvatar());
        categoryVo.setCategoryName(category.getCategoryName());
        categoryVo.setDescription(category.getDescription());
        log.info("[goo-blog|CategoryServiceImpl|listCategoryById] CategoryVo 属性设置完成！正在返回...");

        return categoryVo;
    }

    /**
     * 查询所有分类
     * @return List<CategoryVo>
     */
    @Override
    public List<CategoryVo> listAllCategory() {

        log.info("[goo-blog|CategoryServiceImpl|listAllCategory] 查询所有分类...");
        List<Category> categories = categoryMapper.selectList(new QueryWrapper<>());

        log.info("[goo-blog|CategoryServiceImpl|listAllCategory] 正在设置vo对象...");
        List<CategoryVo> result = copyList(categories);
        log.info("[goo-blog|CategoryServiceImpl|listAllCategory] vo对象属性设置完成！正在返回...");

        return result;
    }

    private List<CategoryVo> copyList(List<Category> categories) {

        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categories) {
            categoryVoList.add(copy(category));
        }
        return  categoryVoList;
    }

    private CategoryVo copy(Category category) {

        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);

        return categoryVo;
    }


}
