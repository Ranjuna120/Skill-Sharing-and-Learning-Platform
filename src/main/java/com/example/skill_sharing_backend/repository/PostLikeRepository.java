package com.example.skill_sharing_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skill_sharing_backend.model.PostLike;
import com.example.skill_sharing_backend.model.PostLikeId;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    List<PostLike> findByPostId(Long postId);
    List<PostLike> findByUserId(Long userId);
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
} 