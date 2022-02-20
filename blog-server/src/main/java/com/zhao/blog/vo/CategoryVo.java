package com.zhao.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @date 2022/1/21 14:23
 * @description Category封装对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Category封装对象")
public class CategoryVo {

    private Long id;

    @ApiModelProperty(value = "类型图标")
    private String avatar;

    @ApiModelProperty(value = "类型名称")
    private String categoryName;

    @ApiModelProperty(value = "类型描述")
    private String description;
}
