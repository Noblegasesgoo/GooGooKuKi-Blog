package com.zhao.blog.controller;


import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.ISysUserService;
import com.zhao.blog.utils.UserThreadLocalUtils;
import com.zhao.blog.vo.params.PasswordParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */

@Slf4j
@RestController
@RequestMapping("/user")
@Api(value = "用户管理", tags = "用户管理")
public class SysUserController {

    @Resource
    private ISysUserService userService;

    @MyLogger(module = "用户管理", operation = "根据token查询对应用户信息请求")
    @GetMapping("/private/current")
    @ApiOperation(value = "根据token查询对应用户信息请求")
    public Response queryCurrentUserInfo(@RequestHeader("Authorization") String token) {

        if (StringUtils.isBlank(token)) {
            log.error("[goo-blog|SysUserController|queryCurrentUserInfo] token不能为空！" + token);
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "token不能为空！", token);
        }

        SysUser user = userService.getCurrentUserInfoByToken(token);
        /** 将当前登陆用户信息缓存到该线程本地内存中 **/
        UserThreadLocalUtils.put(user);

        if (null == user) {
            log.error("[goo-blog|SysUserController|queryCurrentUserInfo] token无效或错误！" + token);
            return new Response(StatusCode.STATUS_CODEC10001.getCode(), "token无效或错误！", user);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询当前登陆用户信息成功！", user);

    }

    @MyLogger(module = "用户管理", operation = "根据id查询对应用户信息请求")
    @GetMapping("/public/user/{id}")
    @ApiOperation(value = "根据id查询对应用户信息请求")
    public Response queryUserInfoById(@PathVariable Long id) {

        if (null == id) {
            log.error("[goo-blog|SysUserController|queryUserInfoById] id不能为空！" + id);
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "必要参数为空！", id);
        }

        SysUser user = userService.selectUserByAuthorId(id);

        if (null == user) {
            log.error("[goo-blog|SysUserController|queryUserInfoById] 未查找到该用户！" + user);
            return new Response(StatusCode.STATUS_CODEC10001.getCode(), "未查找到该用户！", user);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "查询用户信息成功！", user);

    }

    @MyLogger(module = "用户管理", operation = "根据用户id查询修改密码请求")
    @PostMapping("/private/update/password")
    @ApiOperation(value = "根据用户id查询修改密码请求")
    public Response updatePasswordForCurrentUser(@RequestBody PasswordParams passwordParams) {

        if (null == passwordParams
                || null == passwordParams.getOldPassword()
                || null == passwordParams.getNewPasswordForInput()
                || null == passwordParams.getNewPasswordForLastInput()) {
            log.error("[goo-blog|SysUserController|updatePasswordForCurrentUser] 必要参数为空！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "必要参数为空！", passwordParams);
        }

        SysUser user = UserThreadLocalUtils.get();
        Boolean result = userService.updatePasswordForCurrentUser(user, passwordParams);

        if (!result) {
            log.error("[goo-blog|SysUserController|updatePasswordForCurrentUser] 两次输入的新密码不一致或数据库错误！");
            return new Response(StatusCode.STATUS_CODEC10001.getCode(), "两次输入的新密码不一致或数据库繁忙！请稍后再试", result);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "密码修改成功！正在跳转至登陆页面...", result);

    }

}
