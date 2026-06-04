package com.main.dto;

import com.main.Model.Game;

public class GameResponseDTO {
    private Long gameId;
    private String title;
    private String coverUrl;
    private double price;
    private double matchScore;

    public GameResponseDTO() {
    }

    public GameResponseDTO(Long gameId, String title, String coverUrl, double price, double matchScore) {
        this.gameId = gameId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.price = price;
        this.matchScore = matchScore;
    }

    public GameResponseDTO(Game game, double matchScore) {
        if (game != null) {
            this.gameId = game.getGameId() != null ? game.getGameId().longValue() : null;
            this.title = game.getTitle();
            this.coverUrl = game.getImageUrl();
            this.price = game.getPrice() != null ? game.getPrice() : 0.0;
            this.matchScore = matchScore;
        }
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}
