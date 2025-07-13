package com.example.service;

import com.example.model.Post;
import com.example.model.User;
import com.example.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
    }

    public Page<Post> getUserPosts(User user, Pageable pageable) {
        return postRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    @Transactional
    public void deletePost(Long id, User currentUser) {
        Post post = getPostById(id);
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    public boolean isPostOwner(Long postId, User user) {
        Post post = getPostById(postId);
        return post.getUser().getId().equals(user.getId());
    }
} 