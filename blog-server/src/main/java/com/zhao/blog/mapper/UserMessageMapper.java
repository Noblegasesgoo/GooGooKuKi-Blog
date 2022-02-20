package com.zhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhao.blog.domain.entity.UserMessage;
import com.zhao.blog.vo.MessageVo;
import com.zhao.blog.vo.params.MessageParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-28
 */

@Repository
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    /**
     * 条件查询当前用户的消息
     * @param id
     * @param messageParams
     * @return List<MessageVo>
     */
    List<MessageVo> selectPrivateMessageForCurrentUser(@Param("id") Long id, @Param("messageParams") MessageParams messageParams);

    ///**
    // * 查询当前用户的评论被回复消息请求
    // * @param id
    // * @param messageParams
    // * @return List<MessageVo>
    // */
    //List<MessageVo> selectPrivateReplyForCurrentUser(@Param("id") Long id, @Param("messageParams") MessageParams messageParams);
    //
    ///**
    // * 查询当前用户的文章被评论消息请求
    // * @param id
    // * @param messageParams
    // * @return List<MessageVo>
    // */
    //List<MessageVo> selectPrivateCommentForCurrentUser(@Param("id") Long id, @Param("messageParams") MessageParams messageParams);
}
