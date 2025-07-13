package com.example.skill_sharing_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.skill_sharing_backend.dto.UserDTO;
import com.example.skill_sharing_backend.model.User;
import com.example.skill_sharing_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
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

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> followUser(
            @RequestParam Long userId,
            @RequestParam Long followedUserId) {
        User user = userService.followUser(followedUserId, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("followersCount", user.getFollowersCount());
        response.put("message", "Successfully followed user");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, Object>> unfollowUser(
            @RequestParam Long userId,
            @RequestParam Long followedUserId) {
        User user = userService.unfollowUser(followedUserId, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("followersCount", user.getFollowersCount());
        response.put("message", "Successfully unfollowed user");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<Map<String, Object>> getFollowers(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("followers", user.getFollowers());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<Map<String, Object>> getFollowing(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("following", user.getFollowing());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/following/{followedId}")
    public ResponseEntity<Map<String, Boolean>> isFollowing(
            @PathVariable Long id,
            @PathVariable Long followedId) {
        User user = userService.getUserById(id);
        User followedUser = userService.getUserById(followedId);
        
        if (user == null || followedUser == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isFollowing = user.getFollowing().contains(followedUser);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully logged out");
        return ResponseEntity.ok(response);
    }
}