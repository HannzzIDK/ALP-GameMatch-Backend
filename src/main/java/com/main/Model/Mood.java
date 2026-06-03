package com.main.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer moodId;
    private String moodName;

    @OneToMany(mappedBy = "mood")
    private List<GameMood> gameMoods;

    public Integer getMoodId() {
        return moodId;
    }

    public void setMoodId(Integer moodId) {
        this.moodId = moodId;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    public List<GameMood> getGameMoods() {
        return gameMoods;
    }

    public void setGameMoods(List<GameMood> gameMoods) {
        this.gameMoods = gameMoods;
    }
}