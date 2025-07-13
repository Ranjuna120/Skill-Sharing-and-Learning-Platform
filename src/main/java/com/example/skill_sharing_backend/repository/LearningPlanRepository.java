package com.example.skill_sharing_backend.repository;



import com.example.skill_sharing_backend.model.LearningPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {
}