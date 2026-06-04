package com.main.Repository;

import com.main.Model.Game;
import com.main.Model.GameTierEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepo extends JpaRepository<Game, Long> {

    @Query("SELECT DISTINCT g FROM Game g " +
           "LEFT JOIN FETCH g.gameGenres gg LEFT JOIN FETCH gg.genre " +
           "LEFT JOIN FETCH g.gameMoods gm LEFT JOIN FETCH gm.mood " +
           "WHERE (:maxPrice IS NULL OR g.price <= :maxPrice) " +
           "AND (:gameTier IS NULL OR g.gameTier = :gameTier)")
    List<Game> findGamesWithFilters(
            @Param("maxPrice") Float maxPrice,
            @Param("gameTier") GameTierEnum gameTier);
}
