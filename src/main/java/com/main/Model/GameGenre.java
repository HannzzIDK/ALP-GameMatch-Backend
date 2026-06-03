package com.main.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_genre")
public class GameGenre {    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameGenreId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Integer getGameGenreId() {
        return gameGenreId;
    }
    public void setGameGenreId(Integer gameGenreId) {
        this.gameGenreId = gameGenreId;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}