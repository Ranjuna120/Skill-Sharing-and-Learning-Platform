package com.example.skill_sharing_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skill_sharing_backend.dto.ProgressUpdateDTO;
import com.example.skill_sharing_backend.model.ProgressUpdate;
import com.example.skill_sharing_backend.repository.ProgressUpdateRepository;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.ProgressUpdateService;

@Service
public class ProgressUpdateServiceImpl implements ProgressUpdateService {

    @Autowired
    private ProgressUpdateRepository updateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ProgressUpdate> getAllUpdates() {
        return updateRepository.findAll();
    }

    @Override
    public ProgressUpdate createUpdate(ProgressUpdateDTO updateDTO, Long userId) {
        ProgressUpdate update = new ProgressUpdate();
        update.setContent(updateDTO.getContent());
        update.setTemplateType(updateDTO.getTemplateType());
        update.setUser(userRepository.findById(userId).orElseThrow());
        update.setCreatedAt(LocalDateTime.now());
        return updateRepository.save(update);
    }

    @Override
    public ProgressUpdate updateUpdate(Long id, ProgressUpdateDTO updateDTO, Long userId) {
        ProgressUpdate update = updateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Progress update not found"));

        // Verify that the user owns this update
        if (!update.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this update");
        }

        update.setContent(updateDTO.getContent());
        update.setTemplateType(updateDTO.getTemplateType());
        return updateRepository.save(update);
    }

    @Override
    public void deleteUpdate(Long id, Long userId) {
        ProgressUpdate update = updateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Progress update not found"));

        // Verify that the user owns this update
        if (!update.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this update");
        }

        updateRepository.delete(update);
    }
}
