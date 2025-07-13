package com.example.skill_sharing_backend.service;

import java.util.List;

import com.example.skill_sharing_backend.model.Notification;

public interface NotificationService {
    List<Notification> getNotificationsForUser(Long userId);
    Notification createCommentNotification(Long userId, Long commenterId, Long postId, Long commentId);
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
} 