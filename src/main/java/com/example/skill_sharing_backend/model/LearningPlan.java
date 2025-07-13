package com.example.skill_sharing_backend.model;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "learning_plans")
@Data
public class LearningPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String topics;
    private String resources;
    private String timeline;
}
