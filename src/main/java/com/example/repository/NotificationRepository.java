package com.example.repository;

import com.example.model.Notification;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    long countByUserAndIsReadFalse(User user);
} 