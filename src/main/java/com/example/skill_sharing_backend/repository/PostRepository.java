package com.example.skill_sharing_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.skill_sharing_backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
}
