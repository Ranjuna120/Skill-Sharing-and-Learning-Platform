package com.example.skill_sharing_backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.skill_sharing_backend.dto.EmailLoginDTO;
import com.example.skill_sharing_backend.dto.UserDTO;
import com.example.skill_sharing_backend.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    ResponseEntity<User> createUser(User user);
    User getUserById(Long id);
    User followUser(Long userId, Long followerId);
    User unfollowUser(Long userId, Long followerId);
    List<UserDTO> searchUsers(String query);
    List<UserDTO> getAllUsers();
    boolean isFollowing(Long userId, Long followedId);
    ResponseEntity<?> login(EmailLoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<?> register(EmailLoginDTO registerDTO);
    User findByEmail(String email);
    ResponseEntity<User> updateUser(User user);
}
