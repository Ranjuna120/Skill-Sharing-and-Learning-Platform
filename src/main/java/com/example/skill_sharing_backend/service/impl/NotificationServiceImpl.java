package com.example.skill_sharing_backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skill_sharing_backend.model.Comment;
import com.example.skill_sharing_backend.model.Notification;
import com.example.skill_sharing_backend.model.Post;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.repository.CommentRepository;
import com.example.skill_sharing_backend.repository.NotificationRepository;
import com.example.skill_sharing_backend.repository.PostRepository;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public Notification createCommentNotification(Long userId, Long commenterId, Long postId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User commenter = userRepository.findById(commenterId)
                .orElseThrow(() -> new RuntimeException("Commenter not found"));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Don't create notification if user is commenting on their own post
        if (userId.equals(commenterId)) {
            return null;
        }

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setCommenter(commenter);
        notification.setPost(post);
        notification.setComment(comment);

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
} 