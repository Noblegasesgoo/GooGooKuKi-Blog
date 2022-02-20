package com.zhao.blog.vo;

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
 * @date 2022/1/28 18:30
 * @description UserMessage封装对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserMessage封装对象")
public class MessageVo {

    private Long id;

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "发送消息的用户")
    private SysUserVo fromUser;

    @ApiModelProperty(value = "接收消息的用户")
    private SysUserVo toUser;

    @ApiModelProperty(value = "消息可能关联的帖子")
    private ArticleVo article;

    @ApiModelProperty(value = "消息可能关联的评论")
    private Long commentId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "消息状态")
    private String messageStatus;

    @ApiModelProperty(value = "记录创建时间")
    private String gmtCreate;
}
