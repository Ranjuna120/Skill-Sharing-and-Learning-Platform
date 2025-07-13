package com.example.skill_sharing_backend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String profileImage;
    private String source;
    private int followersCount;
    private int followingCount;
    private boolean isFollowing;
}