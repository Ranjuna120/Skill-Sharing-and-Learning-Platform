package com.example.skill_sharing_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user who receives the notification

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    private User commenter;  // The user who made the comment

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // The post that was commented on

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;  // The actual comment

    @Column(name = "is_read")
    private boolean read;
    
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        read = false;
    }
} 