package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.UserMessage;
import com.zhao.blog.mapper.UserMessageMapper;
import com.zhao.blog.service.IUserMessageService;
import com.zhao.blog.vo.MessageVo;
import com.zhao.blog.vo.params.MessageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zhao.blog.common.consts.StaticData.NUMBER_ZERO;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author noblegasesgoo
 * @since 2022-01-28
 */

@Slf4j
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

    @Autowired
    private UserMessageMapper messageMapper;

    /**
     * 条件查询当前用户的消息
     * @param id
     * @param messageParams
     * @return List<MessageVo>
     */
    @Override
    public List<MessageVo> listPrivateMessageForCurrentUser(Long id, MessageParams messageParams) {

        log.debug("[goo-blog|UserMessageServiceImpl|listPrivateMessageForCurrentUser] 根据id和状态查询对应的消息中...");
        List<MessageVo> messageVos = messageMapper.selectPrivateMessageForCurrentUser(id, messageParams);

        log.debug("[goo-blog|UserMessageServiceImpl|listPrivateMessageForCurrentUser] 查询成功正在返回...");
        return messageVos;
    }

    /**
     * 确认消息
     * @param messageParams
     * @return 是否修改成功
     */
    @Override
    public Boolean updateMessageStatus(MessageParams messageParams) {

        UserMessage userMessage = new UserMessage().setId(messageParams.getId()).setMessageStatus(messageParams.getStatus());

        int i = messageMapper.updateById(userMessage);

        if (i == NUMBER_ZERO) {
            return false;
        }

        return true;
    }

    ///**
    // * 查询当前用户的评论被回复消息请求
    // * @param id
    // * @param messageParams
    // * @return List<MessageVo>
    // */
    //@Override
    //public List<MessageVo> listPrivateReplyForCurrentUser(Long id, MessageParams messageParams) {
    //
    //    log.debug("[goo-blog|UserMessageServiceImpl|listPrivateReplyForCurrentUser] 根据id和状态查询对应的回复消息中...");
    //    List<MessageVo> messageVos = messageMapper.selectPrivateReplyForCurrentUser(id, messageParams);
    //
    //    log.debug("[goo-blog|UserMessageServiceImpl|listPrivateReplyForCurrentUser] 查询成功正在返回...");
    //    return messageVos;
    //}
    //
    ///**
    // * 查询当前用户的文章被评论消息请求
    // * @param id
    // * @param messageParams
    // * @return List<MessageVo>
    // */
    //@Override
    //public List<MessageVo> listPrivateCommentForCurrentUser(Long id, MessageParams messageParams) {
    //    log.debug("[goo-blog|UserMessageServiceImpl|listPrivateReplyForCurrentUser] 根据id和状态查询对应的回复消息中...");
    //    List<MessageVo> messageVos = messageMapper.selectPrivateCommentForCurrentUser(id, messageParams);
    //
    //    log.debug("[goo-blog|UserMessageServiceImpl|listPrivateReplyForCurrentUser] 查询成功正在返回...");
    //    return messageVos;
    //}
}
