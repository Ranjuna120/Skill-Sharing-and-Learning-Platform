package com.example.skill_sharing_backend.service.impl;



import com.example.skill_sharing_backend.model.Badge;
import com.example.skill_sharing_backend.repository.BadgeRepository;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BadgeServiceImpl implements BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void awardBadge(Long userId, String badgeName) {
        Badge badge = new Badge();
        badge.setUser(userRepository.findById(userId).orElseThrow());
        badge.setBadgeName(badgeName);
        badge.setAwardedAt(LocalDateTime.now());
        badgeRepository.save(badge);
    }
}
