package com.main.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String email;
    private String googleAccountId;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleAccountId() {
        return googleAccountId;
    }

    public void setGoogleAccountId(String googleAccountId) {
        this.googleAccountId = googleAccountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToMany(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinTable(name = "favorites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
    private java.util.Set<Game> favoriteGames = new java.util.HashSet<>();

    public java.util.Set<Game> getFavoriteGames() {
        return this.favoriteGames;
    }

    @Column(name = "is_quiz_completed", nullable = false)
    private Boolean isQuizCompleted = false;

    public Boolean isQuizCompleted() {
        return isQuizCompleted;
    }

    public void setQuizCompleted(Boolean isQuizCompleted) {
        this.isQuizCompleted = isQuizCompleted;
    }
}
