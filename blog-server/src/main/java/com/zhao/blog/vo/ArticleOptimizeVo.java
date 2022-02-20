package com.zhao.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/27 17:54
 * @description Article优化版vo
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Article封装对象优化版")
public class ArticleOptimizeVo {

    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "浏览数量")
    private Integer viewCounts;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCounts;

    @ApiModelProperty(value = "是否置顶")
    private Boolean isTop;

    @ApiModelProperty(value = "是否禁用")
    private Boolean isDeleted;

    //@ApiModelProperty(value = "记录创建时间")
    //@DateTimeFormat(pattern="yyyy-MM-dd")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    //private Date gmtCreate;

    @ApiModelProperty(value = "记录创建时间")
    //@DateTimeFormat(pattern="yyyy-MM-dd")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private String gmtCreate;


    @ApiModelProperty(value = "文章对应作者id")
    private SysUserVo author;

    @ApiModelProperty(value = "文章对应内容VO对象")
    private ArticleBodyVo body;

    @ApiModelProperty(value = "文章对应标签VO对象列表")
    private List<TagVo> tags;

    @ApiModelProperty(value = "文章对应分类对象列表")
    private CategoryVo category;
}
