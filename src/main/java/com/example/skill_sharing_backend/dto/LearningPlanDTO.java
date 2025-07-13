package com.example.skill_sharing_backend.dto;

import lombok.Data;

@Data
public class LearningPlanDTO {
    private Long id;
    private String title;
    private String topics;
    private String resources;
    private String timeline;
    private Long userId;
    private String userName;
}