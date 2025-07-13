package com.example.service;

import com.example.model.*;
import com.example.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void createLikeNotification(User recipient, User actor, Post post) {
        if (recipient.equals(actor)) {
            return; // Don't notify users about their own actions
        }

        Notification notification = new Notification();
        notification.setUser(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setType("LIKE");
        notification.setMessage(actor.getUsername() + " liked your post");
        notificationRepository.save(notification);
    }

    @Transactional
    public void createCommentNotification(User recipient, User actor, Post post) {
        if (recipient.equals(actor)) {
            return;
        }

        Notification notification = new Notification();
        notification.setUser(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setType("COMMENT");
        notification.setMessage(actor.getUsername() + " commented on your post");
        notificationRepository.save(notification);
    }

    @Transactional
    public void createFollowNotification(User recipient, User actor) {
        if (recipient.equals(actor)) {
            return;
        }

        Notification notification = new Notification();
        notification.setUser(recipient);
        notification.setActor(actor);
        notification.setType("FOLLOW");
        notification.setMessage(actor.getUsername() + " started following you");
        notificationRepository.save(notification);
    }

    public Page<Notification> getUserNotifications(User user, Pageable pageable) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public long getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void markAllNotificationsAsRead(User user) {
        Page<Notification> notifications = getUserNotifications(user, Pageable.unpaged());
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
} 