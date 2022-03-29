package com.zhao.blog.vo.params;

import com.zhao.blog.vo.CategoryVo;
import com.zhao.blog.vo.TagVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/24 12:23
 * @description 文章发布参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文章发布参数")
public class ArticleParams {

    @ApiModelProperty(value = "文章id")
    @Min(value = 0, message = "必须为非负数")
    private Long articleId;

    @ApiModelProperty(value = "文章作者id")
    @Min(value = 0, message = "必须为非负数")
    private Long authorId;

    @ApiModelProperty(value = "文章标题")
    @Length(min = 1, max = 50)
    private String title;

    @ApiModelProperty(value = "文章摘要")
    @Length(min = 1, max = 180)
    private String summary;

    @ApiModelProperty(value = "文章内容")
    private ArticleBodyParams body;

    @ApiModelProperty(value = "文章类别")
    private CategoryVo category;

    @ApiModelProperty(value = "文章标签")
    private List<TagVo> tags;
}
