package com.main.Games;
import org.springframework.data.repository.CrudRepository;

public interface GenreGameRepo extends CrudRepository <GameGenre, Integer> {
    
}
