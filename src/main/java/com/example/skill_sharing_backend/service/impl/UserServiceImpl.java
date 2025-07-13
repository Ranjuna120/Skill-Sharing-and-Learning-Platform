package com.example.skill_sharing_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skill_sharing_backend.dto.EmailLoginDTO;
import com.example.skill_sharing_backend.dto.UserDTO;
import com.example.skill_sharing_backend.model.RegistrationSource;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.repository.UserRepository;
import com.example.skill_sharing_backend.service.UserService;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<User> createUser(User user) {
        // Set default values for counts
        user.setFollowersCount(0);
        user.setFollowingCount(0);
        
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            // Update existing user's information
            existingUser.setName(user.getName());
            existingUser.setProfileImage(user.getProfileImage());
            existingUser.setSource(user.getSource());
            return ResponseEntity.ok(userRepository.save(existingUser));
        }
        
        // Create new user
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public User followUser(Long userId, Long followerId) {
        User user = getUserById(userId);
        User follower = getUserById(followerId);

        if (!user.getFollowers().contains(follower)) {
            user.getFollowers().add(follower);
            user.setFollowersCount(user.getFollowersCount() + 1);
            follower.setFollowingCount(follower.getFollowingCount() + 1);
            userRepository.save(follower);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User unfollowUser(Long userId, Long followerId) {
        User user = getUserById(userId);
        User follower = getUserById(followerId);

        if (user.getFollowers().contains(follower)) {
            user.getFollowers().remove(follower);
            user.setFollowersCount(user.getFollowersCount() - 1);
            follower.setFollowingCount(follower.getFollowingCount() - 1);
            userRepository.save(follower);
        }

        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFollowing(Long userId, Long followedId) {
        User user = getUserById(userId);
        User followed = getUserById(followedId);
        return user.getFollowing().contains(followed);
    }

    @Override
    public ResponseEntity<?> login(EmailLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user == null || user.getSource() != RegistrationSource.CREDENTIAL) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Initialize counts if not already set
        user.setFollowersCount(user.getFollowersCount() < 0 ? 0 : user.getFollowersCount());
        user.setFollowingCount(user.getFollowingCount() < 0 ? 0 : user.getFollowingCount());

        // Convert to DTO before returning
        UserDTO dto = convertToDTO(user);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<?> register(EmailLoginDTO registerDTO) {
        User existingUser = userRepository.findByEmail(registerDTO.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered");
        }

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getEmail().split("@")[0]); // Use email username as default name
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setSource(RegistrationSource.CREDENTIAL);
        
        User savedUser = userRepository.save(user);
        
        // Convert to DTO before returning
        UserDTO dto = convertToDTO(savedUser);
        return ResponseEntity.ok(dto);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        if (user.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfileImage(user.getProfileImage());
        dto.setSource(user.getSource().toString());
        dto.setFollowersCount(user.getFollowersCount());
        dto.setFollowingCount(user.getFollowingCount());
        return dto;
    }
}
