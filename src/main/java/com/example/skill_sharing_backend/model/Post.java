package com.example.skill_sharing_backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String description;

    @Lob
    private byte[] image1;
    @Lob
    private byte[] image2;
    @Lob
    private byte[] image3;
    @Lob
    private byte[] video;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "favorite_count")
    private Integer favoriteCount = 0;

    @Column(name = "share_count")
    private Integer shareCount = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_likes",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedBy = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_favorites",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> favoritedBy = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_shares",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> sharedBy = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Post() {
        this.createdAt = LocalDateTime.now();
        this.likeCount = 0;
        this.favoriteCount = 0;
        this.shareCount = 0;
        this.likedBy = new ArrayList<>();
        this.favoritedBy = new ArrayList<>();
        this.sharedBy = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // Add methods to safely access collections
    public List<User> getLikedBy() {
        return likedBy != null ? new ArrayList<>(likedBy) : new ArrayList<>();
    }

    public List<User> getFavoritedBy() {
        return favoritedBy != null ? new ArrayList<>(favoritedBy) : new ArrayList<>();
    }

    public List<User> getSharedBy() {
        return sharedBy != null ? new ArrayList<>(sharedBy) : new ArrayList<>();
    }

    public List<Comment> getComments() {
        return comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }

    // Add method to safely get user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Safe getters for counts
    public int getLikeCount() {
        return likeCount != null ? likeCount : 0;
    }

    public int getFavoriteCount() {
        return favoriteCount != null ? favoriteCount : 0;
    }

    public int getShareCount() {
        return shareCount != null ? shareCount : 0;
    }

    // Safe setters for counts
    public void setLikeCount(int count) {
        this.likeCount = count;
    }

    public void setFavoriteCount(int count) {
        this.favoriteCount = count;
    }

    public void setShareCount(int count) {
        this.shareCount = count;
    }
}