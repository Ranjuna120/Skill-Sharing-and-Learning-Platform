package com.example.repository;

import com.example.model.Comment;
import com.example.model.Post;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);
    Page<Comment> findByUser(User user, Pageable pageable);
    Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);
    long countByPost(Post post);
} 