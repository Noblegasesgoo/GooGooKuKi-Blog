package com.zhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.blog.domain.entity.UserMessage;
import com.zhao.blog.vo.MessageVo;
import com.zhao.blog.vo.params.MessageParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-28
 */
public interface IUserMessageService extends IService<UserMessage> {

    /**
     * 条件查询当前用户的消息
     * @param id
     * @param messageParams
     * @return List<MessageVo>
     */
    List<MessageVo> listPrivateMessageForCurrentUser(Long id, MessageParams messageParams);

    /**
     * 确认消息
     * @param messageParams
     * @return 是否修改成功
     */
    Boolean updateMessageStatus(MessageParams messageParams);


    ///**
    // * 查询当前用户的文章被评论消息请求
    // * @param id
    // * @param messageParams
    // * @return List<MessageVo>
    // */
    //List<MessageVo> listPrivateReplyForCurrentUser(Long id, MessageParams messageParams);
    //
    ///**
    // * 查询当前用户的文章被回复消息请求
    // * @param id
    // * @param messageParams
    // * @return List<MessageVo>
    // */
    //List<MessageVo> listPrivateCommentForCurrentUser(Long id, MessageParams messageParams);
}
