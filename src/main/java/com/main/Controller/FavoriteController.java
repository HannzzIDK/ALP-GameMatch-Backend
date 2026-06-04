package com.main.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.main.Model.Favorite;
import com.main.Model.Game;
import com.main.Model.User;
import com.main.Repository.FavoriteRepo;
import com.main.Repository.GameRepo;
import com.main.Repository.UserRepo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepo favoriteRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GameRepo gameRepo;

    @PostMapping("/{gameId}")
    public String addFavorite(@PathVariable Long gameId, @RequestParam Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        Game game = gameRepo.findById(gameId).orElse(null);
        if (user == null || game == null) {
            return "User or Game not found";
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setGame(game);
        favorite.setSavedAt(LocalDateTime.now());
        favoriteRepo.save(favorite);
        return "Added to favorites";
    }

    @DeleteMapping("/{gameId}")
    public String removeFavorite(@PathVariable Long gameId, @RequestParam Long userId) {
        Optional<Favorite> favorite = favoriteRepo.findByUser_UserIdAndGame_GameId(userId.intValue(),
                gameId.intValue());
        if (favorite.isPresent()) {
            favoriteRepo.delete(favorite.get());
            return "Removed from favorites";
        }
        return "Favorite not found";
    }

    @GetMapping("/")
    public Iterable<Game> getFavorites(@RequestParam Long userId) {
        List<Favorite> favorites = favoriteRepo.findByUser_UserId(userId.intValue());
        List<Game> games = new ArrayList<>();
        for (Favorite fav : favorites) {
            games.add(fav.getGame());
        }
        return games;
    }
}