package com.zhao.blog.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/10 10:01
 * @description 有关文章的消息
 */

@Data
public class ArticleMessage implements Serializable {

    @ApiModelProperty(value = "文章id")
    private Long articleId;

    @ApiModelProperty(value = "文章消息")
    private String message;
}
