package com.example.skill_sharing_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_sharing_backend.dto.EmailLoginDTO;
import com.example.skill_sharing_backend.dto.UserDTO;
import com.example.skill_sharing_backend.model.RegistrationSource;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.service.UserService;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<UserDTO> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Object principal = authentication.getPrincipal();
        User user = null;

        if (principal instanceof OAuth2User) {
            // Handle Google OAuth2 user
            OAuth2User oauth2User = (OAuth2User) principal;
            String email = oauth2User.getAttribute("email");
            user = userService.findByEmail(email);
            
            if (user != null) {
                // Update existing user's information
                user.setName(oauth2User.getAttribute("name"));
                user.setProfileImage(oauth2User.getAttribute("picture"));
                ResponseEntity<User> updateResponse = userService.updateUser(user);
                user = updateResponse.getBody();
            } else {
                // Create new user if doesn't exist
                User newUser = new User();
                newUser.setName(oauth2User.getAttribute("name"));
                newUser.setEmail(email);
                newUser.setProfileImage(oauth2User.getAttribute("picture"));
                newUser.setSource(RegistrationSource.GOOGLE);
                newUser.setPassword(""); // Empty password for Google users
                ResponseEntity<User> createResponse = userService.createUser(newUser);
                user = createResponse.getBody();
            }
        } else if (principal instanceof User) {
            // Handle manual login user
            user = (User) principal;
        }

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // Convert to DTO
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfileImage(user.getProfileImage());
        dto.setSource(user.getSource().toString());
        dto.setFollowersCount(user.getFollowersCount());
        dto.setFollowingCount(user.getFollowingCount());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody EmailLoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(loginDTO, request, response);
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@RequestBody EmailLoginDTO registerDTO) {
        return userService.register(registerDTO);
    }
    
    @GetMapping("/api/auth/session-test")
    public ResponseEntity<String> sessionTest(HttpServletRequest request) {
        return ResponseEntity.ok("Session ID: " + request.getSession().getId() + 
                                ", User: " + SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Clear the security context
            SecurityContextHolder.clearContext();
            
            // Invalidate the session
            if (request.getSession(false) != null) {
                request.getSession().invalidate();
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }
}