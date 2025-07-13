package com.example.skill_sharing_backend.service;



import com.example.skill_sharing_backend.dto.CommentDTO;
import com.example.skill_sharing_backend.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByPostId(Long postId);
    Comment addComment(Long postId, CommentDTO commentDTO, Long userId);
    Comment updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
}