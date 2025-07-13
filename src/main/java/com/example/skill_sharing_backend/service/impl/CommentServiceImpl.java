package com.example.skill_sharing_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skill_sharing_backend.dto.CommentDTO;
import com.example.skill_sharing_backend.model.Comment;
import com.example.skill_sharing_backend.repository.CommentRepository;
import com.example.skill_sharing_backend.repository.PostRepository;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.CommentService;

@SuppressWarnings("unused")
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment addComment(Long postId, CommentDTO commentDTO, Long userId) {
        Comment comment = new Comment();
        comment.setPost(postRepository.findById(postId).orElseThrow());
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.setContent(commentDTO.getContent());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}