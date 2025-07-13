package com.example.skill_sharing_backend.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.skill_sharing_backend.dto.PostDTO;
import com.example.skill_sharing_backend.model.Post;
import com.example.skill_sharing_backend.model.PostInteraction;
import com.example.skill_sharing_backend.model.PostInteraction.InteractionType;
import com.example.skill_sharing_backend.model.PostLike;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.repository.PostInteractionRepository;
import com.example.skill_sharing_backend.repository.PostLikeRepository;
import com.example.skill_sharing_backend.repository.PostRepository;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.PostService;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostInteractionRepository postInteractionRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        try {
            List<Post> posts = (List<Post>) postRepository.findAllByOrderByCreatedAtDesc();
            List<Post> validPosts = new ArrayList<>();
            
            for (Post post : posts) {
                try {
                    // Initialize user eagerly
                    if (post.getUser() == null) {
                        logger.warn("Post {} has no associated user, skipping", post.getId());
                        continue;
                    }
                    
                    // Initialize collections safely
                    post.setLikedBy(new ArrayList<>(post.getLikedBy()));
                    post.setFavoritedBy(new ArrayList<>(post.getFavoritedBy()));
                    post.setSharedBy(new ArrayList<>(post.getSharedBy()));
                    post.setComments(new ArrayList<>(post.getComments()));
                    
                    // Initialize counts safely
                    post.setLikeCount(post.getLikedBy().size());
                    post.setFavoriteCount(post.getFavoritedBy().size());
                    post.setShareCount(post.getSharedBy().size());
                    
                    validPosts.add(post);
                } catch (Exception e) {
                    logger.error("Error initializing post {}: {}", post.getId(), e.getMessage());
                }
            }
            
            return validPosts;
        } catch (Exception e) {
            logger.error("Error in getAllPosts: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch posts: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Post createPost(PostDTO postDTO, List<MultipartFile> images, MultipartFile video, Long userId) {
        logger.info("Creating post with title: '{}' for user: {}", postDTO.getTitle(), userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        
        // Process images
        if (images != null && !images.isEmpty()) {
            logger.info("Processing {} images for post", images.size());
            int imageCount = 0;
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    try {
                        byte[] imageBytes = image.getBytes();
                        switch (imageCount) {
                            case 0:
                                post.setImage1(imageBytes);
                                break;
                            case 1:
                                post.setImage2(imageBytes);
                                break;
                            case 2:
                                post.setImage3(imageBytes);
                                break;
                            default:
                                logger.warn("Ignoring additional image as maximum of 3 images is supported");
                                break;
                        }
                        imageCount++;
                        logger.debug("Successfully processed image {}: {}", imageCount, image.getOriginalFilename());
                    } catch (IOException e) {
                        logger.error("Failed to process image {}: {}", image.getOriginalFilename(), e.getMessage());
                        throw new RuntimeException("Failed to process image: " + image.getOriginalFilename(), e);
                    }
                }
            }
            logger.info("Successfully processed {} images for post", imageCount);
        }
        
        // Process video
        if (video != null && !video.isEmpty()) {
            try {
                logger.info("Processing video: {}", video.getOriginalFilename());
                post.setVideo(video.getBytes());
                logger.debug("Successfully processed video: {}", video.getOriginalFilename());
            } catch (IOException e) {
                logger.error("Failed to process video {}: {}", video.getOriginalFilename(), e.getMessage());
                throw new RuntimeException("Failed to process video: " + video.getOriginalFilename(), e);
            }
        }
        
        Post savedPost = postRepository.save(post);
        logger.info("Successfully created post with ID: {}", savedPost.getId());
        return savedPost;
    }

    @Override
    public Post updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public boolean deletePost(Long id, Long userId) {
        try {
            logger.info("Attempting to delete post {} by user {}", id, userId);
            
            Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
            
            // Check if the user is the owner of the post
            if (!post.getUser().getId().equals(userId)) {
                logger.warn("User {} attempted to delete post {} owned by user {}", 
                    userId, id, post.getUser().getId());
                return false;
            }
            
            // Delete all interactions first
            postInteractionRepository.deleteAll(
                postInteractionRepository.findByPostId(id)
            );
            
            // Now delete the post
            postRepository.delete(post);
            logger.info("Successfully deleted post {} by user {}", id, userId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting post {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete post: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Post likePost(Long id, Long userId) {
        try {
            Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Check if already liked
            if (!postLikeRepository.existsByPostIdAndUserId(id, userId)) {
                PostLike postLike = new PostLike();
                postLike.setPost(post);
                postLike.setUser(user);
                postLikeRepository.save(postLike);

                // Update post like count
                post.setLikeCount(post.getLikeCount() + 1);
                post.getLikedBy().add(user);
                return postRepository.save(post);
            }
            return post;
        } catch (Exception e) {
            logger.error("Error liking post {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to like post: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Post unlikePost(Long id, Long userId) {
        try {
            Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Check if liked
            if (postLikeRepository.existsByPostIdAndUserId(id, userId)) {
                postLikeRepository.deleteByPostIdAndUserId(id, userId);

                // Update post like count
                post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
                post.getLikedBy().remove(user);
                return postRepository.save(post);
            }
            return post;
        } catch (Exception e) {
            logger.error("Error unliking post {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to unlike post: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Post favoritePost(Long id, Long userId) {
        return handleInteraction(id, userId, InteractionType.FAVORITE, true);
    }

    @Override
    @Transactional
    public Post unfavoritePost(Long id, Long userId) {
        return handleInteraction(id, userId, InteractionType.FAVORITE, false);
    }

    @Override
    @Transactional
    public Post sharePost(Long id, Long userId) {
        return handleInteraction(id, userId, InteractionType.SHARE, true);
    }

    @Override
    @Transactional
    public Post unsharePost(Long id, Long userId) {
        return handleInteraction(id, userId, InteractionType.SHARE, false);
    }

    private Post handleInteraction(Long postId, Long userId, InteractionType type, boolean add) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (add) {
            if (!postInteractionRepository.existsByUserIdAndPostIdAndInteractionType(userId, postId, type)) {
                PostInteraction interaction = new PostInteraction();
                interaction.setPost(post);
                interaction.setUser(user);
                interaction.setInteractionType(type);
                postInteractionRepository.save(interaction);

                // Update counts
                switch (type) {
                    case LIKE -> {
                        post.setLikeCount(post.getLikeCount() + 1);
                        post.getLikedBy().add(user);
                    }
                    case FAVORITE -> {
                        post.setFavoriteCount(post.getFavoriteCount() + 1);
                        post.getFavoritedBy().add(user);
                    }
                    case SHARE -> {
                        post.setShareCount(post.getShareCount() + 1);
                        post.getSharedBy().add(user);
                    }
                }
            }
        } else {
            postInteractionRepository.findByUserIdAndPostIdAndInteractionType(userId, postId, type)
                .ifPresent(interaction -> {
                    postInteractionRepository.delete(interaction);

                    // Update counts
                    switch (type) {
                        case LIKE -> {
                            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
                            post.getLikedBy().remove(user);
                        }
                        case FAVORITE -> {
                            post.setFavoriteCount(Math.max(0, post.getFavoriteCount() - 1));
                            post.getFavoritedBy().remove(user);
                        }
                        case SHARE -> {
                            post.setShareCount(Math.max(0, post.getShareCount() - 1));
                            post.getSharedBy().remove(user);
                        }
                    }
                });
        }

        return postRepository.save(post);
    }

    @Override
    public List<User> getLikedByUsers(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getLikedBy();
    }

    @Override
    public List<User> getFavoritedByUsers(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getFavoritedBy();
    }

    @Override
    public List<User> getSharedByUsers(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getSharedBy();
    }

    @Override
    public boolean hasUserLiked(Long postId, Long userId) {
        return postInteractionRepository.existsByUserIdAndPostIdAndInteractionType(userId, postId, InteractionType.LIKE);
    }

    @Override
    public boolean hasUserFavorited(Long postId, Long userId) {
        return postInteractionRepository.existsByUserIdAndPostIdAndInteractionType(userId, postId, InteractionType.FAVORITE);
    }

    @Override
    public boolean hasUserShared(Long postId, Long userId) {
        return postInteractionRepository.existsByUserIdAndPostIdAndInteractionType(userId, postId, InteractionType.SHARE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(Long userId) {
        try {
            List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
            List<Post> validPosts = new ArrayList<>();
            
            for (Post post : posts) {
                try {
                    // Initialize collections safely
                    post.setLikedBy(new ArrayList<>(post.getLikedBy()));
                    post.setFavoritedBy(new ArrayList<>(post.getFavoritedBy()));
                    post.setSharedBy(new ArrayList<>(post.getSharedBy()));
                    post.setComments(new ArrayList<>(post.getComments()));
                    
                    // Initialize counts safely
                    post.setLikeCount(post.getLikedBy().size());
                    post.setFavoriteCount(post.getFavoritedBy().size());
                    post.setShareCount(post.getSharedBy().size());
                    
                    validPosts.add(post);
                } catch (Exception e) {
                    logger.error("Error initializing post {}: {}", post.getId(), e.getMessage());
                }
            }
            
            return validPosts;
        } catch (Exception e) {
            logger.error("Error in getPostsByUserId: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch posts for user: " + e.getMessage(), e);
        }
    }
}