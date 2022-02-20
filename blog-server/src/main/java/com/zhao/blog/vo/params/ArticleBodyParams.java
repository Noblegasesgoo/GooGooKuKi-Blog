package com.zhao.blog.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/24 12:24
 * @description 文章内容参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文章内容参数")
public class ArticleBodyParams {

    @ApiModelProperty(value = "文章内容id")
    private Long id;

    @ApiModelProperty(value = "文章md格式内容")
    private String content;

    @ApiModelProperty(value = "文章html格式内容")
    private String contentHtml;
}
