package com.example.skill_sharing_backend.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeId implements Serializable {
    private Long post;
    private Long user;
} 