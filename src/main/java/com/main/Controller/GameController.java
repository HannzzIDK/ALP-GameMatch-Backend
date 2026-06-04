package com.main.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.main.Model.Game;
import com.main.Repository.GameRepo;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {

    @Autowired
    private GameRepo gameRepo;

    @GetMapping("/{gameId}")
    public Game getGameById(@PathVariable Long gameId) {
        return gameRepo.findById(gameId).orElse(null);
    }

    @GetMapping("/match")
    public Iterable<Game> matchGames() {
        return gameRepo.findAll();
    }
}