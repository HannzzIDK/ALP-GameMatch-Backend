package com.main.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genreId;
    private String genreName;

    @OneToMany(mappedBy = "genre")
    private List<GameGenre> gameGenres;

    public Integer getGenreId() {
        return genreId;
    }
    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public List<GameGenre> getGameGenres() {
        return gameGenres;
    }
    public void setGameGenres(List<GameGenre> gameGenres) {
        this.gameGenres = gameGenres;
    }
}