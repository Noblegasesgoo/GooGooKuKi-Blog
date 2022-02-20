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
 * @date 2022/2/7 10:32
 * @description 权限参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="权限参数")
public class PermissionParams {

    @ApiModelProperty(value = "权限id")
    private Long id;

    @ApiModelProperty(value = "许可名称")
    private String permissionName;

    @ApiModelProperty(value = "许可路径")
    private String path;

    @ApiModelProperty(value = "许可描述")
    private String description;
}
