package com.example.repository;

import com.example.model.Follow;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);
    Page<Follow> findByFollower(User follower, Pageable pageable);
    Page<Follow> findByFollowing(User following, Pageable pageable);
    Page<Follow> findByFollowerOrderByCreatedAtDesc(User follower, Pageable pageable);
    Page<Follow> findByFollowingOrderByCreatedAtDesc(User following, Pageable pageable);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
    long countByFollower(User follower);
    long countByFollowing(User following);
} 