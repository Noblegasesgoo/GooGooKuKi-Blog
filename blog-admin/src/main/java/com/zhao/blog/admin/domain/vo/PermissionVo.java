package com.zhao.blog.admin.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/6 15:29
 * @description PermissionVo对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PermissionVo对象")
public class PermissionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "许可名称")
    private String permissionName;

    @ApiModelProperty(value = "许可路径")
    private String path;

    @ApiModelProperty(value = "许可描述")
    private String description;

    @ApiModelProperty(value = "是否禁用")
    private Boolean isDeleted;

}
