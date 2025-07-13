package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Like;
import com.example.model.Post;
import com.example.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);
    List<Like> findByUser(User user);
    Optional<Like> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
} 