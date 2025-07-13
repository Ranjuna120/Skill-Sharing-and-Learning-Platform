package com.example.skill_sharing_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProgressUpdateDTO {
    private Long id;
    private String content;
    private String templateType;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
}
