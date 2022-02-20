package com.zhao.blog.vo.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/19 11:25
 * @description 分页属性
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="分页属性")
public class PageParams {

    /** 默认页数 **/
    @ApiModelProperty(value = "默认页数")
    private Integer page;

    /** 默认页面大小 **/
    @ApiModelProperty(value = "默认页面大小")
    private Integer pageSize;

    /** 文章对应类别id **/
    @ApiModelProperty(value = "文章对应类别id")
    private Long categoryId;

    /** 文章对应标签id **/
    @ApiModelProperty(value = "文章对应标签id")
    private Long tagId;

    /** 文章对应标签id **/
    @ApiModelProperty(value = "文章对应作者id")
    private Long authorId;

    /** 文章对应归档日期 **/
    @ApiModelProperty(value = "文章对应归档日期")
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "Asia/Shanghai")
    private String date;
}
