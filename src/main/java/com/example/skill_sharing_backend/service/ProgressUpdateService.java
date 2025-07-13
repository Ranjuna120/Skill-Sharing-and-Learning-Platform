package com.example.skill_sharing_backend.service;

import java.util.List;

import com.example.skill_sharing_backend.dto.ProgressUpdateDTO;
import com.example.skill_sharing_backend.model.ProgressUpdate;

public interface ProgressUpdateService {
    List<ProgressUpdate> getAllUpdates();
    ProgressUpdate createUpdate(ProgressUpdateDTO updateDTO, Long userId);
    ProgressUpdate updateUpdate(Long id, ProgressUpdateDTO updateDTO, Long userId);
    void deleteUpdate(Long id, Long userId);
}
