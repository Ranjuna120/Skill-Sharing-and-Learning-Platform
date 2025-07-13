package com.example.skill_sharing_backend.service.impl;



import com.example.skill_sharing_backend.dto.LearningPlanDTO;
import com.example.skill_sharing_backend.model.LearningPlan;
import com.example.skill_sharing_backend.repository.LearningPlanRepository;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.LearningPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningPlanServiceImpl implements LearningPlanService {

    @Autowired
    private LearningPlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<LearningPlan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public LearningPlan createPlan(LearningPlanDTO planDTO, Long userId) {
        LearningPlan plan = new LearningPlan();
        plan.setTitle(planDTO.getTitle());
        plan.setTopics(planDTO.getTopics());
        plan.setResources(planDTO.getResources());
        plan.setTimeline(planDTO.getTimeline());
        plan.setUser(userRepository.findById(userId).orElseThrow());
        return planRepository.save(plan);
    }

    @Override
    public LearningPlan updatePlan(Long id, LearningPlanDTO planDTO) {
        LearningPlan plan = planRepository.findById(id).orElseThrow();
        plan.setTitle(planDTO.getTitle());
        plan.setTopics(planDTO.getTopics());
        plan.setResources(planDTO.getResources());
        plan.setTimeline(planDTO.getTimeline());
        return planRepository.save(plan);
    }

    @Override
    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }
}