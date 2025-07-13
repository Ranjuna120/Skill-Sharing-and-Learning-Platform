package com.example.controller;

import com.example.model.*;
import com.example.service.NotificationService;
import com.example.service.SocialInteractionService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/social")
@RequiredArgsConstructor
@Validated
public class SocialController {
    private final SocialInteractionService socialService;
    private final NotificationService notificationService;
    private final UserService userService;

    // Follow endpoints
    @PostMapping("/users/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> toggleFollow(
            @PathVariable Long userId,
            @AuthenticationPrincipal User follower) {
        try {
            if (userId.equals(follower.getId())) {
                return ResponseEntity.badRequest().build();
            }
            User following = userService.getUserById(userId);
            socialService.toggleFollow(follower, following);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users/{userId}/following")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long userId,
            @AuthenticationPrincipal User follower) {
        try {
            User following = userService.getUserById(userId);
            return ResponseEntity.ok(socialService.isFollowing(follower, following));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<Page<Follow>> getUserFollowers(
            @PathVariable Long userId,
            Pageable pageable) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(socialService.getUserFollowers(user, pageable));
    }

    @GetMapping("/users/{userId}/following-list")
    public ResponseEntity<Page<Follow>> getUserFollowing(
            @PathVariable Long userId,
            Pageable pageable) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(socialService.getUserFollowing(user, pageable));
    }

    // User favorites
    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<Page<Favorite>> getUserFavorites(
            @PathVariable Long userId,
            Pageable pageable) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(socialService.getUserFavorites(user, pageable));
    }

    // Notification endpoints
    @GetMapping("/notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Notification>> getNotifications(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUserNotifications(user, pageable));
    }

    @GetMapping("/notifications/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadNotificationCount(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(user));
    }

    @PostMapping("/notifications/{notificationId}/read")
    @PreAuthorize("@notificationService.isNotificationOwner(#notificationId, #user)")
    public ResponseEntity<Void> markNotificationAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User user) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/mark-all-read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAllNotificationsAsRead(
            @AuthenticationPrincipal User user) {
        notificationService.markAllNotificationsAsRead(user);
        return ResponseEntity.ok().build();
    }
}