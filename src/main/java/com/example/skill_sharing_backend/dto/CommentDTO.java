package com.example.skill_sharing_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
    private String userProfileImage;
    private Long postId;
}
