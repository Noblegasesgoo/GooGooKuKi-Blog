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
 * @date 2022/2/12 15:06
 * @description es查询参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="es查询参数")
public class EsParams {

    /** 默认页数 **/
    @ApiModelProperty(value = "页数")
    private Integer page;

    /** 默认页面大小 **/
    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    /** 文章对应类别id **/
    @ApiModelProperty(value = "中文条件查询条件")
    private String condition;
}
