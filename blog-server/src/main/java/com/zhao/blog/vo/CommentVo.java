package com.zhao.blog.vo;

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
 * @date 2022/1/22 18:26
 * @description Comments封装对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Comments封装对象")
public class CommentVo {

    private Long id;

    @ApiModelProperty(value = "评论等级")
    private Integer level;

    @ApiModelProperty(value = "评论父级id")
    private Long parentId;

    @ApiModelProperty(value = "谁在评论")
    private SysUserVo author;

    @ApiModelProperty(value = "给谁评论")
    private SysUserVo toUser;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "是否禁用")
    private Boolean isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    private String gmtCreate;
}
