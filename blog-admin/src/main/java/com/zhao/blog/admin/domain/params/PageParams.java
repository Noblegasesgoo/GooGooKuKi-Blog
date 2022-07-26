package com.zhao.blog.admin.domain.params;

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
 * @date 2022/2/6 15:22
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

    /** 查询条件 **/
    @ApiModelProperty(value = "权限名称")
    private String permissionName;

}
