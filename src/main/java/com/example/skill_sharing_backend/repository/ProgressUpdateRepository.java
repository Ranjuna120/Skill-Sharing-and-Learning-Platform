package com.example.skill_sharing_backend.repository;



import com.example.skill_sharing_backend.model.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, Long> {
}