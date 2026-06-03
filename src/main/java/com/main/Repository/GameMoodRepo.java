package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.GameMood;
public interface GameMoodRepo extends JpaRepository<GameMood, Integer> {

}
