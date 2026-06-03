package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.GameGenre;
public interface GenreGameRepo extends JpaRepository <GameGenre, Integer> {
    
}
