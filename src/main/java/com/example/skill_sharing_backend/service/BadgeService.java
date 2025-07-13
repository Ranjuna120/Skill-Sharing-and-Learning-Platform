package com.example.skill_sharing_backend.service;



import com.example.skill_sharing_backend.model.Badge;

@SuppressWarnings("unused")
public interface BadgeService {
    void awardBadge(Long userId, String badgeName);
}
