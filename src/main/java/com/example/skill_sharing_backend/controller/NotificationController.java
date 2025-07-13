package com.example.skill_sharing_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_sharing_backend.model.Notification;
import com.example.skill_sharing_backend.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsForUser(userId);
            List<Map<String, Object>> notificationDtos = notifications.stream()
                .map(notification -> {
                    Map<String, Object> notificationMap = new HashMap<>();
                    notificationMap.put("id", notification.getId());
                    notificationMap.put("commenterId", notification.getCommenter().getId());
                    notificationMap.put("commenterName", notification.getCommenter().getName());
                    notificationMap.put("commenterImage", notification.getCommenter().getProfileImage());
                    notificationMap.put("postId", notification.getPost().getId());
                    notificationMap.put("createdAt", notification.getCreatedAt());
                    notificationMap.put("read", notification.isRead());
                    return notificationMap;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(notificationDtos);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch notifications: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to mark notification as read: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete notification: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
} 