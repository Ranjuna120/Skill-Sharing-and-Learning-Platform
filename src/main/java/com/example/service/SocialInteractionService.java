package com.example.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Comment;
import com.example.model.Favorite;
import com.example.model.Follow;
import com.example.model.Like;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.CommentRepository;
import com.example.repository.FavoriteRepository;
import com.example.repository.FollowRepository;
import com.example.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialInteractionService {
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final NotificationService notificationService;

    // Like operations
    @Transactional
    public void toggleLike(User user, Post post) {
        if (likeRepository.existsByUserAndPost(user, post)) {
            likeRepository.deleteByUserAndPost(user, post);
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            notificationService.createLikeNotification(post.getUser(), user, post);
        }
    }

    public boolean hasUserLiked(User user, Post post) {
        return likeRepository.existsByUserAndPost(user, post);
    }

    public long getLikeCount(Post post) {
        return likeRepository.countByPost(post);
    }

    // Comment operations
    @Transactional
    public Comment addComment(User user, Post post, String content) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);
        Comment savedComment = commentRepository.save(comment);
        notificationService.createCommentNotification(post.getUser(), user, post);
        return savedComment;
    }

    @Transactional
    public void updateComment(Long commentId, String newContent) {
        commentRepository.findById(commentId).ifPresent(comment -> {
            comment.setContent(newContent);
            commentRepository.save(comment);
        });
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Page<Comment> getPostComments(Post post, Pageable pageable) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable);
    }

    public boolean isCommentOwner(Long commentId, User user) {
        return commentRepository.findById(commentId)
            .map(comment -> comment.getUser().getId().equals(user.getId()))
            .orElse(false);
    }

    public Long getCommentPostId(Long commentId) {
        return commentRepository.findById(commentId)
            .map(comment -> comment.getPost().getId())
            .orElse(null);
    }

    // Favorite operations
    @Transactional
    public void toggleFavorite(User user, Post post) {
        if (favoriteRepository.existsByUserAndPost(user, post)) {
            favoriteRepository.deleteByUserAndPost(user, post);
        } else {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setPost(post);
            favoriteRepository.save(favorite);
        }
    }

    public boolean hasUserFavorited(User user, Post post) {
        return favoriteRepository.existsByUserAndPost(user, post);
    }

    public Page<Favorite> getUserFavorites(User user, Pageable pageable) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    // Follow operations
    @Transactional
    public void toggleFollow(User follower, User following) {
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            followRepository.deleteByFollowerAndFollowing(follower, following);
        } else {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
            notificationService.createFollowNotification(following, follower);
        }
    }

    public boolean isFollowing(User follower, User following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    public Page<Follow> getUserFollowers(User user, Pageable pageable) {
        return followRepository.findByFollowingOrderByCreatedAtDesc(user, pageable);
    }

    public Page<Follow> getUserFollowing(User user, Pageable pageable) {
        return followRepository.findByFollowerOrderByCreatedAtDesc(user, pageable);
    }

    public long getFollowerCount(User user) {
        return followRepository.countByFollowing(user);
    }

    public long getFollowingCount(User user) {
        return followRepository.countByFollower(user);
    }
}