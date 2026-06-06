package com.main.Controller;

import com.main.Model.Game;
import com.main.Repository.GameRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Wajib agar JS tidak diblokir
public class GameController {

    private final GameRepo gameRepo;

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameRepo.findAll());
    }
}