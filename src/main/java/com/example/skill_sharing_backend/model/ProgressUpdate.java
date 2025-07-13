package com.example.skill_sharing_backend.model;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress_updates")
@Data
public class ProgressUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private String templateType;
    private LocalDateTime createdAt;
}