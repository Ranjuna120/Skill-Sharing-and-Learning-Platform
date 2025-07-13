package com.example.skill_sharing_backend.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.skill_sharing_backend.dto.PostDTO;
import com.example.skill_sharing_backend.model.Post;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            List<Map<String, Object>> dtos = new ArrayList<>();
            
            for (Post post : posts) {
                try {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", post.getId());
                    dto.put("title", post.getTitle());
                    dto.put("description", post.getDescription());
                    dto.put("createdAt", post.getCreatedAt());
                    dto.put("likeCount", post.getLikeCount());
                    dto.put("favoriteCount", post.getFavoriteCount());
                    dto.put("shareCount", post.getShareCount());
                    
                    // Handle images and video safely
                    try {
                        if (post.getImage1() != null) {
                            dto.put("image1", Base64.getEncoder().encodeToString(post.getImage1()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding image1 for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    try {
                        if (post.getImage2() != null) {
                            dto.put("image2", Base64.getEncoder().encodeToString(post.getImage2()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding image2 for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    try {
                        if (post.getImage3() != null) {
                            dto.put("image3", Base64.getEncoder().encodeToString(post.getImage3()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding image3 for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    try {
                        if (post.getVideo() != null) {
                            dto.put("video", Base64.getEncoder().encodeToString(post.getVideo()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding video for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    // Create user object in the format expected by frontend
                    User user = post.getUser();
                    if (user != null) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("name", user.getName());
                        userMap.put("profileImage", user.getProfileImage());
                        dto.put("user", userMap);
                    } else {
                        logger.warn("Post {} has no associated user", post.getId());
                        continue; // Skip posts without users
                    }
                    
                    // Add interaction lists
                    dto.put("likedBy", post.getLikedBy().stream().map(User::getId).collect(java.util.stream.Collectors.toList()));
                    dto.put("favoritedBy", post.getFavoritedBy().stream().map(User::getId).collect(java.util.stream.Collectors.toList()));
                    dto.put("sharedBy", post.getSharedBy().stream().map(User::getId).collect(java.util.stream.Collectors.toList()));
                    
                    // Add comments
                    dto.put("comments", post.getComments().stream().map(comment -> {
                        Map<String, Object> commentMap = new HashMap<>();
                        commentMap.put("id", comment.getId());
                        commentMap.put("content", comment.getContent());
                        commentMap.put("createdAt", comment.getCreatedAt());
                        commentMap.put("userId", comment.getUser().getId());
                        commentMap.put("userName", comment.getUser().getName());
                        commentMap.put("userProfileImage", comment.getUser().getProfileImage());
                        return commentMap;
                    }).collect(java.util.stream.Collectors.toList()));
                    
                    dtos.add(dto);
                } catch (Exception e) {
                    logger.error("Error converting post {} to DTO: {}", post.getId(), e.getMessage());
                }
            }
            
            if (dtos.isEmpty() && !posts.isEmpty()) {
                logger.error("Failed to convert any posts to DTOs");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to convert posts to DTOs"));
            }
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("Error fetching posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to fetch posts: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "video", required = false) MultipartFile video,
            @RequestParam("userId") Long userId) {
        try {
            logger.info("Received create post request - Title: {}, Description length: {}, UserId: {}, Images: {}, Video: {}", 
                title,
                description.length(),
                userId,
                images != null ? images.stream().map(MultipartFile::getOriginalFilename).toList() : "null",
                video != null ? video.getOriginalFilename() : "null"
            );

            // Validate input
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Title is required"));
            }
            if (description == null || description.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Description is required"));
            }
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User ID is required"));
            }

            // Create PostDTO
            PostDTO postDTO = new PostDTO();
            postDTO.setTitle(title.trim());
            postDTO.setDescription(description.trim());

            // Create post
            Post post = postService.createPost(postDTO, images, video, userId);
            
            // Convert the response to avoid lazy loading issues
            Map<String, Object> response = new HashMap<>();
            response.put("id", post.getId());
            response.put("title", post.getTitle());
            response.put("description", post.getDescription());
            response.put("createdAt", post.getCreatedAt());
            
            // Handle images
            if (post.getImage1() != null) {
                response.put("image1", Base64.getEncoder().encodeToString(post.getImage1()));
            }
            if (post.getImage2() != null) {
                response.put("image2", Base64.getEncoder().encodeToString(post.getImage2()));
            }
            if (post.getImage3() != null) {
                response.put("image3", Base64.getEncoder().encodeToString(post.getImage3()));
            }
            
            // Handle video
            if (post.getVideo() != null) {
                response.put("video", Base64.getEncoder().encodeToString(post.getVideo()));
            }
            
            // Add user info
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", post.getUser().getId());
            userMap.put("name", post.getUser().getName());
            userMap.put("profileImage", post.getUser().getProfileImage());
            response.put("user", userMap);
            
            logger.info("Successfully created post with ID: {}", post.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating post: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Failed to create post",
                    "message", e.getMessage(),
                    "details", e.getClass().getSimpleName()
                ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        Post updatedPost = postService.updatePost(id, postDTO);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        if (postService.deletePost(id, userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long id, @RequestParam Long userId) {
        Post post = postService.likePost(id, userId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long userId) {
        try {
            List<Post> posts = postService.getPostsByUserId(userId);
            List<Map<String, Object>> dtos = new ArrayList<>();
            
            for (Post post : posts) {
                try {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", post.getId());
                    dto.put("title", post.getTitle());
                    dto.put("description", post.getDescription());
                    dto.put("createdAt", post.getCreatedAt());
                    dto.put("likeCount", post.getLikeCount());
                    dto.put("favoriteCount", post.getFavoriteCount());
                    dto.put("shareCount", post.getShareCount());
                    
                    // Handle images and video safely
                    try {
                        if (post.getImage1() != null) {
                            dto.put("image1", Base64.getEncoder().encodeToString(post.getImage1()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding image1 for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    try {
                        if (post.getImage2() != null) {
                            dto.put("image2", Base64.getEncoder().encodeToString(post.getImage2()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding image2 for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    try {
                        if (post.getImage3() != null) {
                            dto.put("image3", Base64.getEncoder().encodeToString(post.getImage3()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding image3 for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    try {
                        if (post.getVideo() != null) {
                            dto.put("video", Base64.getEncoder().encodeToString(post.getVideo()));
                        }
                    } catch (Exception e) {
                        logger.warn("Error encoding video for post {}: {}", post.getId(), e.getMessage());
                    }
                    
                    // Create user object in the format expected by frontend
                    User user = post.getUser();
                    if (user != null) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("name", user.getName());
                        userMap.put("profileImage", user.getProfileImage());
                        dto.put("user", userMap);
                    } else {
                        logger.warn("Post {} has no associated user", post.getId());
                        continue; // Skip posts without users
                    }
                    
                    // Add interaction lists
                    dto.put("likedBy", post.getLikedBy().stream().map(User::getId).collect(java.util.stream.Collectors.toList()));
                    dto.put("favoritedBy", post.getFavoritedBy().stream().map(User::getId).collect(java.util.stream.Collectors.toList()));
                    dto.put("sharedBy", post.getSharedBy().stream().map(User::getId).collect(java.util.stream.Collectors.toList()));
                    
                    // Add comments
                    dto.put("comments", post.getComments().stream().map(comment -> {
                        Map<String, Object> commentMap = new HashMap<>();
                        commentMap.put("id", comment.getId());
                        commentMap.put("content", comment.getContent());
                        commentMap.put("createdAt", comment.getCreatedAt());
                        commentMap.put("userId", comment.getUser().getId());
                        commentMap.put("userName", comment.getUser().getName());
                        commentMap.put("userProfileImage", comment.getUser().getProfileImage());
                        return commentMap;
                    }).collect(java.util.stream.Collectors.toList()));
                    
                    dtos.add(dto);
                } catch (Exception e) {
                    logger.error("Error converting post {} to DTO: {}", post.getId(), e.getMessage());
                }
            }
            
            if (dtos.isEmpty() && !posts.isEmpty()) {
                logger.error("Failed to convert any posts to DTOs");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to convert posts to DTOs"));
            }
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("Error fetching posts for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to fetch posts: " + e.getMessage()));
        }
    }
}