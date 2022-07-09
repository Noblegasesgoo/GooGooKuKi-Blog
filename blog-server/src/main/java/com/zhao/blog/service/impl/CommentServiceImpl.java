package com.zhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.blog.domain.entity.Article;
import com.zhao.blog.domain.entity.Comment;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.domain.entity.UserMessage;
import com.zhao.blog.mail.IThreadPoolForMailService;
import com.zhao.blog.mapper.CommentMapper;
import com.zhao.blog.service.IArticleService;
import com.zhao.blog.service.ICommentService;
import com.zhao.blog.service.ISysUserService;
import com.zhao.blog.service.IUserMessageService;
import com.zhao.blog.vo.CommentVo;
import com.zhao.blog.vo.params.CommentParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zhao.blog.common.consts.StaticData.NUMBER_ONE;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IUserMessageService userMessageService;

    @Autowired
    private IArticleService articleService;

    /** 发送邮件使用 **/
    @Autowired
    private IThreadPoolForMailService threadPoolForMailService;

    /** 获取用户信息使用 **/
    @Autowired
    private ISysUserService userService;

    /**
     * 查询对应文章的所有一级评论
     * @param id
     * @return List<CommentVo>
     */
    @Override
    public List<CommentVo> listAllCommentsForCurrentArticleLevelOne(Long id) {

        /** 查询所有当前文章评论 **/
        List<CommentVo> commentVos = commentMapper.selectAllCommentsForCurrentArticleLevelOne(id);

        return commentVos;
    }

    /**
     * 创建一条新评论|回复一条评论
     * @param commentParams
     * @param user
     * @return Boolean
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Boolean createComment(CommentParams commentParams, SysUser user) {

        /** 获得被评论人的所有信息（主要使用邮箱）**/
        SysUser toUser = userService.getById(commentParams.getToUserId());

        Comment comment = new Comment();
        UserMessage userMessage = new UserMessage();

        /** 设置被评论文章的id，将评论与文章关联 **/
        comment.setArticleId(commentParams.getArticleId());
        userMessage.setArticleId(commentParams.getArticleId());

        /** 设置创建该评论的用户id **/
        comment.setAuthorId(user.getId());
        userMessage.setFromUserId(user.getId());

        /** 设置评论内容 **/
        comment.setCommentContent(commentParams.getCommentContent());
        userMessage.setContent(commentParams.getCommentContent());

        /** 查询当前评论文章的对应文章 **/
        Article authorIdByArticleId = articleService.getAuthorIdByArticleId(commentParams.getArticleId());

        /** 查看评论等级那就代表着这条评论是新建的 **/
        if (NUMBER_ONE == commentParams.getLevel()
                || null == commentParams.getParentId()) {
            /** 由于是新的评论，所以等级设置为1 **/
            comment.setLevel(commentParams.getLevel());
            comment.setToUid(authorIdByArticleId.getAuthorId());

            /** 由于是新的评论，设置消息类型为1 **/
            userMessage.setType(1);
            userMessage.setToUserId(authorIdByArticleId.getAuthorId());

            /** 发送评论邮件（异步） **/
            threadPoolForMailService.sendEmailForComment(toUser.getEmail()
                    , "有闸总锐评了君の精彩美文，他说：" + commentParams.getCommentContent()
                            + "\n快来查看:https://www.blog.googookuki.cn/#/article/" + commentParams.getArticleId()
                    ,"来自伟大站长的回复邮件提醒");
        } else {
            /** 设置评论等级为2，并且将评论双向绑定被回复用户 **/
            comment.setParentId(commentParams.getParentId());
            comment.setToUid(commentParams.getToUserId());
            comment.setLevel(commentParams.getLevel());

            /** 由于是新的回复，设置消息类型为2 **/
            userMessage.setType(2);
            userMessage.setToUserId(commentParams.getToUserId());

            /** 发送评论邮件（异步） **/
            threadPoolForMailService.sendEmailForComment(toUser.getEmail()
                    , "有闸总嘴硬了君の美丽发言，他说：" + commentParams.getCommentContent()
                            + "\n快来查看:https://www.blog.googookuki.cn/#/article/" + commentParams.getArticleId()
                    ,"来自伟大站长的回复邮件提醒");
        }

        /** 将该评论插入数据库 **/
        int result = commentMapper.insert(comment);

        /** 同时向消息表中加入新的评论通知 **/
        /** 如果不是自己评论自己文章，就提示消息 **/
        if (!(commentParams.getToUserId() == user.getId() && authorIdByArticleId.getAuthorId() == user.getId())) {

            userMessage.setCommentId(comment.getId());
            userMessage.setMessageStatus("未读");

            boolean save = userMessageService.save(userMessage);

            if (!save) {
                return false;
            }
        }

        if (result == NUMBER_ZERO) {
            return false;
        }

        return true;
    }

    /**
     * 查询对应文章的所有二级评论
     *
     * @param id
     * @return List<CommentVo>
     */
    @Override
    public List<CommentVo> listAllCommentsForCurrentArticleLevelTwo(Long id) {
        /** 查询所有当前文章评论 **/
        List<CommentVo> commentVos = commentMapper.selectAllCommentsForCurrentArticleLevelTow(id);

        return commentVos;
    }

    /**
     * 删除评论
     *
     * @param commentParams
     * @return Boolean
     */
    @Override
    public Boolean deletedComment(CommentParams commentParams) {

        Comment comment = new Comment();
        comment.setId(commentParams.getId());
        comment.setIsDeleted(true);

        int result = commentMapper.updateById(comment);

        if (result == NUMBER_ZERO) {

            return false;
        }

        return true;
    }

}
