package com.zhao.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.ILoginService;
import com.zhao.blog.service.ISysUserService;
import com.zhao.blog.utils.JWTUtils;
import com.zhao.blog.vo.params.LoginParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhao.blog.common.consts.StaticData.NUMBER_ZERO;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/20 14:02
 * @description 登陆服务实现类
 */

@Slf4j
@Service
@Transactional
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private ISysUserService userService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    private String salt = "zhaoliminwanglu1314@#$%^$#@";

    /**
     * 登陆并返回token
     *
     * @param loginParams
     * @return token
     */
    @Override
    public String login(LoginParams loginParams) {

        /** 获取必要属性 **/
        String account = loginParams.getAccount();
        String phoneNumber = loginParams.getPhoneNumber();
        String email = loginParams.getEmail();
        /** 获取当前传入密码加密过后的密码 **/
        String md5Password = DigestUtils.md5Hex(loginParams.getPassword() + salt);

        if (null != phoneNumber && !phoneNumber.equals("")) {

            /** 手机号码登陆 **/
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
            sysUserQueryWrapper.eq("phone_number", phoneNumber)
                    .last("limit 1");
            List<SysUser> users = userService.list(sysUserQueryWrapper);

            if (users.size() > NUMBER_ZERO
                    && md5Password.equals(users.get(NUMBER_ZERO).getPassword())
                    && !users.get(NUMBER_ZERO).getIsDeleted()) {

                String token = JWTUtils.createToken(users.get(NUMBER_ZERO).getId());

                /** 装入 Redis 中 **/
                redisTemplate.opsForValue().set("TOKEN_" + token
                        , JSON.toJSONString(users.get(NUMBER_ZERO))
                        , 1
                        , TimeUnit.DAYS);

                /** 返回登陆用户的信息 **/
                return token;
            }
        } else if (null != email && !email.equals("")) {

            /** 邮箱登陆 **/
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
            sysUserQueryWrapper.eq("email", email)
                    .last("limit 1");
            List<SysUser> users = userService.list(sysUserQueryWrapper);

            if (users.size() > NUMBER_ZERO
                    && md5Password.equals(users.get(NUMBER_ZERO).getPassword())
                    && !users.get(NUMBER_ZERO).getIsDeleted()) {

                String token = JWTUtils.createToken(users.get(NUMBER_ZERO).getId());

                /** 装入 Redis 中 **/
                redisTemplate.opsForValue().set("TOKEN_" + token
                        , JSON.toJSONString(users.get(NUMBER_ZERO))
                        , 1
                        , TimeUnit.DAYS);

                /** 返回登陆用户的信息 **/
                return token;
            }
        } else {

            /** 账号登陆 **/
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
            sysUserQueryWrapper.eq("account", account)
                    .last("limit 1");
            List<SysUser> users = userService.list(sysUserQueryWrapper);

            if (users.size() > NUMBER_ZERO
                    && md5Password.equals(users.get(NUMBER_ZERO).getPassword())
                    && !users.get(NUMBER_ZERO).getIsDeleted()) {

                String token = JWTUtils.createToken(users.get(NUMBER_ZERO).getId());

                /** 装入 Redis 中 **/
                redisTemplate.opsForValue().set("TOKEN_" + token
                        , JSON.toJSONString(users.get(NUMBER_ZERO))
                        , 1
                        , TimeUnit.DAYS);

                /** 返回登陆用户的信息 **/
                return token;
            }
        }

        return null;
    }

    /**
     * 注销
     *
     * @param token
     * @return Boolean
     */
    @Override
    public Boolean logout(String token) {

        Boolean result = redisTemplate.delete("TOKEN_" + token);
        ;

        return result;
    }

    /**
     * 查找token对应的用户信息（检查token合法性）
     *
     * @param token
     * @return SysUser
     */
    @Override
    public SysUser checkToken(String token) {


        /** 检查 是否为空 **/
        if (StringUtils.isBlank(token)) {
            return null;
        }

        /** 检查 token密钥 **/
        if (null == JWTUtils.checkToken(token)) {
            return null;
        }

        /** 检查 redis **/
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (null == userJson) {
            return null;
        }

        SysUser user = JSON.parseObject(userJson, SysUser.class);

        return user;
    }

}

