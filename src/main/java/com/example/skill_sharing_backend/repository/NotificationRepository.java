package com.example.skill_sharing_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.skill_sharing_backend.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(Long userId, boolean read);
} 