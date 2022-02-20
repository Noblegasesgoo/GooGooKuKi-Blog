package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.Category;
import com.zhao.blog.vo.CategoryVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-21
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 根据id查询对应的文章分类
     * @param categoryId
     * @return CategoryVo
     */
    CategoryVo listCategoryById(Long categoryId);

    /**
     * 查询所有分类
     * @return List<CategoryVo>
     */
    List<CategoryVo> listAllCategory();

}
