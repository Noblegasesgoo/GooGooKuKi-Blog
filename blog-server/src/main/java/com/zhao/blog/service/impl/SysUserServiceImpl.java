package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.mapper.SysUserMapper;
import com.zhao.blog.service.ILoginService;
import com.zhao.blog.service.ISysUserService;
import com.zhao.blog.vo.params.PasswordParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.zhao.blog.common.consts.StaticData.NUMBER_ZERO;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ILoginService loginService;

    private String salt = "zhaoliminwanglu1314@#$%^$#@";

    /**
     * 根据作者id查询作者信息
     * @param authorId
     * @return SysUser
     */
    @Override
    public SysUser selectUserByAuthorId(Long authorId) {

        log.debug("[goo-blog|SysUserServiceImpl|selectUserByAuthorId] 开始查找对应文章的user信息...");
        SysUser result = sysUserMapper.selectById(authorId);
        log.debug("[goo-blog|SysUserServiceImpl|selectUserByAuthorId] 查找结束！");

        log.debug("[goo-blog|SysUserServiceImpl|selectUserByAuthorId] 该文章作者是否有昵称?");
        if (null != result
                && null == result.getNickname()
                && result.getNickname().equalsIgnoreCase("")) {
            log.debug("[goo-blog|SysUserServiceImpl|selectUserByAuthorId] 设置默认昵称...");
            result.setNickname("未命名用户");
            log.debug("[goo-blog|SysUserServiceImpl|selectUserByAuthorId] 默认昵称设置结束！");
        }

        return result;
    }

    /**
     * 根据token查询对应用户信息
     * @param token
     * @return SysUser
     */
    @Override
    public SysUser getCurrentUserInfoByToken(String token) {

        SysUser user = loginService.checkToken(token);

        return user;
    }

    /**
     * 根据token修改对应用户密码
     * @param user
     * @param passwordParams
     * @return Boolean
     */
    @Override
    public Boolean updatePasswordForCurrentUser(SysUser user, PasswordParams passwordParams) {

        String oldPassword = passwordParams.getOldPassword();
        String newPasswordForInput = passwordParams.getNewPasswordForInput();
        String newPasswordForLastInput = passwordParams.getNewPasswordForLastInput();

        if (!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(oldPassword + salt))
                || !StringUtils.equals(newPasswordForInput, newPasswordForLastInput)) {
            log.error("[goo-blog|SysUserServiceImpl|updatePasswordForCurrentUser] 旧密码匹配失败或者新密码两次输入不一致！");
            return false;
        }

        user.setPassword(DigestUtils.md5Hex(newPasswordForLastInput + salt));
        int i = sysUserMapper.updateById(user);
        if (i == NUMBER_ZERO) {
            log.error("[goo-blog|SysUserServiceImpl|updatePasswordForCurrentUser] 数据库繁忙！");
            return false;
        }

        return true;
    }

}
