package com.example.skill_sharing_backend.dto;

import java.util.Map;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String description;
    private String image1; // Base64-encoded string
    private String image2; // Base64-encoded string
    private String image3; // Base64-encoded string
    private String video;  // Base64-encoded string
    private Map<String, Object> user;
}