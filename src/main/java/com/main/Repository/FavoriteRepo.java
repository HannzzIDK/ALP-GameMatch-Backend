package com.main.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.Favorite;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepo extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser_UserId(Integer userId);

    Optional<Favorite> findByUser_UserIdAndGame_GameId(Integer userId, Integer gameId);
}