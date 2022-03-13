package com.zhao.blog.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/20 14:04
 * @description 登陆参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="登陆参数")
public class LoginParams {

    @ApiModelProperty(value = "用户名")
    @Length(min = 0, max = 64)
    private String account;

    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @ApiModelProperty(value = "电话号码")
    @Length(min = 0, max = 12)
    private String phoneNumber;

    @ApiModelProperty(value = "密码")
    @Length(min = 0, max = 64)
    private String password;

}
