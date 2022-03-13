package com.zhao.blog.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

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
    @Min(value = 0, message = "必须为非负数")
    private Long id;

    @ApiModelProperty(value = "评论人id")
    @Min(value = 0, message = "必须为非负数")
    private Long fromUserId;

    @ApiModelProperty(value = "评论文章id")
    @Min(value = 0, message = "必须为非负数")
    private Long articleId;

    @ApiModelProperty(value = "被评论人id")
    @Min(value = 0, message = "必须为非负数")
    private Long toUserId;

    @ApiModelProperty(value = "评论父级id")
    @Min(value = 0, message = "必须为非负数")
    private Long parentId;

    @ApiModelProperty(value = "等级")
    @Min(value = 0, message = "必须为非负数")
    private Integer level;

    @ApiModelProperty(value = "评论内容")
    @Length(min = 1, max = 300)
    private String commentContent;
}
