package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.vo.params.PasswordParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-18
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据作者id查询作者信息
     * @param authorId
     * @return SysUser
     */
    SysUser selectUserByAuthorId(Long authorId);


    /**
     * 根据token查询对应用户信息
     * @param token
     * @return SysUser
     */
    SysUser getCurrentUserInfoByToken(String token);

    /**
     * 根据token修改对应用户密码
     *
     * @param user
     * @param passwordParams
     * @return Boolean
     */
    Boolean updatePasswordForCurrentUser(SysUser user, PasswordParams passwordParams);
}
