package com.zhao.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goo_user_message")
@ApiModel(value="UserMessage对象", description="")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "发送消息的用户ID")
    @TableField("from_user_id")
    private Long fromUserId;

    @ApiModelProperty(value = "接收消息的用户ID")
    @TableField("to_user_id")
    private Long toUserId;

    @ApiModelProperty(value = "消息可能关联的帖子")
    @TableField("article_id")
    private Long articleId;

    @ApiModelProperty(value = "消息可能关联的评论")
    @TableField("comment_id")
    private Long commentId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "消息状态")
    @TableField("message_status")
    private String messageStatus;

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "是否禁用")
    @TableField("is_deleted")
    private Integer isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "记录最后更新时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


}
