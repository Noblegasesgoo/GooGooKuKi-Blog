package com.zhao.blog.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/20 14:58
 * @description 密码有关参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="密码有关参数")
public class PasswordParams {

    @ApiModelProperty(value = "旧密码")
    @Length(min = 0, max = 64)
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @Length(min = 0, max = 64)
    private String newPasswordForInput;

    @ApiModelProperty(value = "确认新密码密码")
    @Length(min = 0, max = 64)
    private String newPasswordForLastInput;
}
