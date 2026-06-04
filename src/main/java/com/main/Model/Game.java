package com.main.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;
    private String imageUrl;
    private Float price;

    @Enumerated(EnumType.STRING)
    private GameTierEnum gameTier;

    @OneToMany(mappedBy = "game")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "game")
    private List<GameGenre> gameGenres;

    @OneToMany(mappedBy = "game")
    private List<GameMood> gameMoods;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public GameTierEnum getGameTier() {
        return gameTier;
    }

    public void setGameTier(GameTierEnum gameTier) {
        this.gameTier = gameTier;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<GameGenre> getGameGenres() {
        return gameGenres;
    }

    public void setGameGenres(List<GameGenre> gameGenres) {
        this.gameGenres = gameGenres;
    }

    public List<GameMood> getGameMoods() {
        return gameMoods;
    }

    public void setGameMoods(List<GameMood> gameMoods) {
        this.gameMoods = gameMoods;
    }

}