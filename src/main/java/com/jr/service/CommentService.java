package com.jr.service;

import com.jr.entity.Comment;
import com.jr.util.PageHelper;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentByPlayerAndGame(Integer gameId, Integer playerId, PageHelper pageHelper);
    List<Comment> getCommentByPost(Integer postId,int indexPage,int pageData);
    Boolean submitComment(Comment comment);
    Boolean deleteComment(Integer CommentId, Integer userId);
}
