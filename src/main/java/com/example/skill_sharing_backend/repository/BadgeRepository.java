package com.example.skill_sharing_backend.repository;



import com.example.skill_sharing_backend.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}