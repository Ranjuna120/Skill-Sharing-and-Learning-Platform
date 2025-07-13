package com.example.skill_sharing_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_sharing_backend.model.PostInteraction;
import com.example.skill_sharing_backend.model.PostInteraction.InteractionType;

@Repository
public interface PostInteractionRepository extends JpaRepository<PostInteraction, Long> {
    Optional<PostInteraction> findByUserIdAndPostIdAndInteractionType(Long userId, Long postId, InteractionType type);
    List<PostInteraction> findByPostIdAndInteractionType(Long postId, InteractionType type);
    List<PostInteraction> findByUserIdAndInteractionType(Long userId, InteractionType type);
    boolean existsByUserIdAndPostIdAndInteractionType(Long userId, Long postId, InteractionType type);
    long countByPostIdAndInteractionType(Long postId, InteractionType type);
    List<PostInteraction> findByPostId(Long postId);
} 