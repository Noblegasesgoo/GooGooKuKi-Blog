package com.zhao.blog.domain.dos;

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
 * @date 2022/1/20 11:00
 * @description 归档文章do
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="归档文章do")
public class ArchivesDo {

    @ApiModelProperty(value = "归档年")
    private Integer year;

    @ApiModelProperty(value = "归档月")
    private Integer month;

    @ApiModelProperty(value = "总数")
    private Long count;
}
