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
 * @date 2022/1/23 17:44
 * @description 评论参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="评论参数")
public class CommentParams {

    @ApiModelProperty(value = "评论id")
    private Long id;

    @ApiModelProperty(value = "评论人id")
    private Long fromUserId;

    @ApiModelProperty(value = "评论文章id")
    private Long articleId;

    @ApiModelProperty(value = "被评论人id")
    private Long toUserId;

    @ApiModelProperty(value = "评论父级id")
    private Long parentId;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;
}
