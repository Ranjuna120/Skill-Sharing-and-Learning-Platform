package com.example.repository;

import com.example.model.Favorite;
import com.example.model.Post;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUser(User user, Pageable pageable);
    Page<Favorite> findByPost(Post post, Pageable pageable);
    Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Optional<Favorite> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
} 