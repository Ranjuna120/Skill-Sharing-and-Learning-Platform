package com.example.skill_sharing_backend.service;



import com.example.skill_sharing_backend.dto.LearningPlanDTO;
import com.example.skill_sharing_backend.model.LearningPlan;

import java.util.List;

public interface LearningPlanService {
    List<LearningPlan> getAllPlans();
    LearningPlan createPlan(LearningPlanDTO planDTO, Long userId);
    LearningPlan updatePlan(Long id, LearningPlanDTO planDTO);
    void deletePlan(Long id);
}