package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User getCurrentUser() {
        // This should be implemented based on your authentication system
        // For now, it throws an exception
        throw new UnsupportedOperationException("getCurrentUser() needs to be implemented");
    }

    public boolean isCurrentUser(Long userId) {
        User currentUser = getCurrentUser();
        return currentUser.getId().equals(userId);
    }
} 