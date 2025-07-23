package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Comment;
import com.example.model.Post;
import com.example.model.User;
import com.example.service.NotificationService;
import com.example.service.PostService;
import com.example.service.SocialInteractionService;
import com.example.skill_sharing_backend.util.StringUtils;
import com.example.skill_sharing_backend.util.ValidationUtils;
import com.example.skill_sharing_backend.util.DateUtils;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {
    private final PostService postService;
    private final SocialInteractionService socialService;
    private final NotificationService notificationService;

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(socialService.getLikeCount(post));
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        try {
            Post post = postService.getPostById(postId);
            socialService.toggleLike(user, post);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{postId}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> hasFavorited(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        try {
            Post post = postService.getPostById(postId);
            return ResponseEntity.ok(socialService.hasUserFavorited(user, post));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{postId}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> toggleFavorite(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        try {
            Post post = postService.getPostById(postId);
            socialService.toggleFavorite(user, post);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<Comment>> getPostComments(
            @PathVariable Long postId,
            Pageable pageable) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(socialService.getPostComments(post, pageable));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user) {
        
        // Use StringUtils to clean and validate comment content
        String cleanedContent = StringUtils.normalizeWhitespace(request.getContent());
        
        // Additional validation using ValidationUtils
        ValidationUtils.ValidationResult lengthValidation = 
            ValidationUtils.validateLength(cleanedContent, "comment", 1, 1000);
        
        if (!lengthValidation.isValid()) {
            return ResponseEntity.badRequest().build();
        }
        
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(socialService.addComment(user, post, cleanedContent));
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("@socialInteractionService.isCommentOwner(#commentId, #user)")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user) {
        socialService.updateComment(commentId, request.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("@socialInteractionService.isCommentOwner(#commentId, #user) or " +
                  "@postService.isPostOwner(@socialInteractionService.getCommentPostId(#commentId), #user)")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user) {
        socialService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    // NEW: Get posts created in the last N days using DateUtils
    @GetMapping("/recent")
    public ResponseEntity<String> getRecentPosts(
            @RequestParam(defaultValue = "7") int days,
            Pageable pageable) {
        
        // Use DateUtils to calculate date range
        LocalDateTime now = DateUtils.getCurrentTimestamp();
        LocalDateTime startDate = DateUtils.addDays(now, -days); // subtract days
        
        String message = String.format("Looking for posts since: %s (Current: %s)", 
                DateUtils.formatDateTime(startDate), 
                DateUtils.formatDateTime(now));
        
        // This would require a new method in PostService
        // return ResponseEntity.ok(postService.getPostsSince(startDate, pageable));
        
        // For now, return a message showing the date calculation
        return ResponseEntity.ok(message);
    }

    @Data
    public static class CommentRequest {
        @NotBlank(message = "Comment content cannot be empty")
        @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
        private String content;
    }
} 