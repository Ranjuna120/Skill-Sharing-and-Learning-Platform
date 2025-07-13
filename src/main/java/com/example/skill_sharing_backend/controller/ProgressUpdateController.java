package com.example.skill_sharing_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_sharing_backend.dto.ProgressUpdateDTO;
import com.example.skill_sharing_backend.model.ProgressUpdate;
import com.example.skill_sharing_backend.service.ProgressUpdateService;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true",
    allowedHeaders = {
        "Authorization", "Content-Type", "X-Requested-With", "accept", "Origin",
        "Access-Control-Request-Method", "Access-Control-Request-Headers"
    },
    exposedHeaders = {
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials"
    }
)
public class ProgressUpdateController {

    @Autowired
    private ProgressUpdateService updateService;

    @GetMapping
    public List<ProgressUpdateDTO> getAllUpdates() {
        List<ProgressUpdate> updates = updateService.getAllUpdates();
        return updates.stream().map(update -> {
            ProgressUpdateDTO dto = new ProgressUpdateDTO();
            dto.setId(update.getId());
            dto.setContent(update.getContent());
            dto.setTemplateType(update.getTemplateType());
            dto.setCreatedAt(update.getCreatedAt());
            dto.setUserId(update.getUser().getId());
            dto.setUserName(update.getUser().getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProgressUpdateDTO> createUpdate(@RequestBody ProgressUpdateDTO updateDTO, @RequestParam Long userId) {
        ProgressUpdate update = updateService.createUpdate(updateDTO, userId);
        ProgressUpdateDTO responseDTO = new ProgressUpdateDTO();
        responseDTO.setId(update.getId());
        responseDTO.setContent(update.getContent());
        responseDTO.setTemplateType(update.getTemplateType());
        responseDTO.setCreatedAt(update.getCreatedAt());
        responseDTO.setUserId(update.getUser().getId());
        responseDTO.setUserName(update.getUser().getName());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgressUpdateDTO> updateUpdate(
            @PathVariable Long id,
            @RequestBody ProgressUpdateDTO updateDTO,
            @RequestParam Long userId) {
        try {
            ProgressUpdate update = updateService.updateUpdate(id, updateDTO, userId);
            ProgressUpdateDTO responseDTO = new ProgressUpdateDTO();
            responseDTO.setId(update.getId());
            responseDTO.setContent(update.getContent());
            responseDTO.setTemplateType(update.getTemplateType());
            responseDTO.setCreatedAt(update.getCreatedAt());
            responseDTO.setUserId(update.getUser().getId());
            responseDTO.setUserName(update.getUser().getName());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUpdate(@PathVariable Long id, @RequestParam Long userId) {
        try {
            updateService.deleteUpdate(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}