package com.main.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_mood")
public class GameMood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameMoodId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    public Integer getGameMoodId() {
        return gameMoodId;
    }

    public void setGameMoodId(Integer gameMoodId) {
        this.gameMoodId = gameMoodId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }
}
