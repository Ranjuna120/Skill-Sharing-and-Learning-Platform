package com.example.skill_sharing_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_sharing_backend.dto.CommentDTO;
import com.example.skill_sharing_backend.model.Comment;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public List<CommentDTO> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return comments.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());
            
            // Ensure user details are properly set
            User user = comment.getUser();
            if (user != null) {
                dto.setUserId(user.getId());
                dto.setUserName(user.getName() != null ? user.getName() : "Anonymous");
                dto.setUserProfileImage(user.getProfileImage());
            } else {
                dto.setUserName("Anonymous");
                dto.setUserProfileImage(null);
            }
            
            dto.setPostId(comment.getPost().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO, @RequestParam Long userId) {
        Comment comment = commentService.addComment(postId, commentDTO, userId);
        CommentDTO responseDTO = new CommentDTO();
        responseDTO.setId(comment.getId());
        responseDTO.setContent(comment.getContent());
        responseDTO.setCreatedAt(comment.getCreatedAt());
        responseDTO.setUserId(comment.getUser().getId());
        responseDTO.setUserName(comment.getUser().getName());
        responseDTO.setUserProfileImage(comment.getUser().getProfileImage());
        responseDTO.setPostId(comment.getPost().getId());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        Comment updatedComment = commentService.updateComment(id, commentDTO);
        CommentDTO responseDTO = new CommentDTO();
        responseDTO.setId(updatedComment.getId());
        responseDTO.setContent(updatedComment.getContent());
        responseDTO.setCreatedAt(updatedComment.getCreatedAt());
        responseDTO.setUserId(updatedComment.getUser().getId());
        responseDTO.setUserName(updatedComment.getUser().getName());
        responseDTO.setUserProfileImage(updatedComment.getUser().getProfileImage());
        responseDTO.setPostId(updatedComment.getPost().getId());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
