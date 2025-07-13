package com.example.skill_sharing_backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.skill_sharing_backend.dto.PostDTO;
import com.example.skill_sharing_backend.model.Post;
import com.example.skill_sharing_backend.model.User;

public interface PostService {
    List<Post> getAllPosts();
    Post createPost(PostDTO postDTO, List<MultipartFile> images, MultipartFile video, Long userId);
    Post updatePost(Long id, PostDTO postDTO);
    boolean deletePost(Long id, Long userId);
    
    // Post interactions
    Post likePost(Long id, Long userId);
    Post unlikePost(Long id, Long userId);
    Post favoritePost(Long id, Long userId);
    Post unfavoritePost(Long id, Long userId);
    Post sharePost(Long id, Long userId);
    Post unsharePost(Long id, Long userId);
    
    // Get interaction lists
    List<User> getLikedByUsers(Long postId);
    List<User> getFavoritedByUsers(Long postId);
    List<User> getSharedByUsers(Long postId);
    
    // Check user interactions
    boolean hasUserLiked(Long postId, Long userId);
    boolean hasUserFavorited(Long postId, Long userId);
    boolean hasUserShared(Long postId, Long userId);

    List<Post> getPostsByUserId(Long userId);
}