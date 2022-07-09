package com.zhao.blog.controller;


import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.IUserMessageService;
import com.zhao.blog.utils.UserThreadLocalUtils;
import com.zhao.blog.vo.MessageVo;
import com.zhao.blog.vo.params.MessageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-28
 */

@Slf4j
@RestController
@RequestMapping("/message")
@Api(value = "消息管理", tags = "消息管理")
public class UserMessageController {

    @Resource
    private IUserMessageService userMessageService;

    @ApiOperation(value = "查询当前用户的消息请求")
    @PostMapping("/private/all")
    public Response queryPrivateMessage(@RequestBody MessageParams messageParams) {

        if (null == messageParams) {
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", messageParams);
        }

        SysUser user = UserThreadLocalUtils.get();
        List<MessageVo> messages = userMessageService.listPrivateMessageForCurrentUser(user.getId(), messageParams);

        if (null == messages) {

            log.debug("[goo-blog|UserMessageController|queryPrivateMessage] 暂无消息！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "暂无消息！", messages);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "当前消息查找成功！", messages);
    }

    @MyLogger(module = "消息管理", operation = "确认消息请求")
    @ApiOperation(value = "确认消息请求")
    @PostMapping("/private/update/status")
    public Response updateMessageStatus(@RequestBody MessageParams messageParams) {

        if (null == messageParams) {
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "缺少必要参数！", messageParams);
        }

        Boolean result = userMessageService.updateMessageStatus(messageParams);

        if (!result) {

            log.debug("[goo-blog|UserMessageController|updateMessageStatus] 确认失败！");
            return new Response(StatusCode.STATUS_CODEC200.getCode(), "数据库繁忙！请重试！", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "确认成功！", result);
    }



}
