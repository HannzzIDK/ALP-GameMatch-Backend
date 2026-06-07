package com.main.Repository;

import com.main.Model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GameRepo extends JpaRepository<Game, Integer> {
    Optional<Game> findByGameId(Integer gameId);
}
