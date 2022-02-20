package com.zhao.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goo_sys_user")
@ApiModel(value="SysUser对象")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String face;

    @ApiModelProperty(value = "邮箱")
    private String email;


    @ApiModelProperty(value = "手机号")
    @TableField("phone_number")
    private String phoneNumber;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "加密盐")
    private String salt;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否管理员")
    @TableField("is_admin")
    private Boolean isAdmin;

    @ApiModelProperty(value = "是否禁用")
    @TableField("is_deleted")
    private Boolean isDeleted;

    @ApiModelProperty(value = "注册时间")
    @TableField("register_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date registerDate;

    @ApiModelProperty(value = "最后登录时间")
    @TableField("last_login_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastLoginDate;

    @ApiModelProperty(value = "记录创建时间")
    @TableField("gmt_create")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date gmtCreate;

    @ApiModelProperty(value = "记录最后更新时间")
    @TableField("gmt_modified")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date gmtModified;


}
