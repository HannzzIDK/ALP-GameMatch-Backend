package com.main.Controller;

import org.springframework.web.bind.annotation.*;
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
        for (Favorite fav : favoriteRepo.findAll()) {
            if (fav.getUser().getUserId().equals(userId.intValue())
                    && fav.getGame().getGameId().equals(gameId.intValue())) {
                favoriteRepo.delete(fav);
                return "Removed from favorites";
            }
        }
        return "Favorite not found";
    }

    @GetMapping("/")
    public Iterable<Game> getFavorites(@RequestParam Long userId) {
        List<Game> games = new ArrayList<>();
        for (Favorite fav : favoriteRepo.findAll()) {
            if (fav.getUser().getUserId().equals(userId.intValue())) {
                games.add(fav.getGame());
            }
        }
        return games;
    }
}