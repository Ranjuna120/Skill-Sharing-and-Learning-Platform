package com.example.skill_sharing_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_interactions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id", "interaction_type"})
    })
@Getter
@Setter
public class PostInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum InteractionType {
        LIKE,
        FAVORITE,
        SHARE
    }
} 