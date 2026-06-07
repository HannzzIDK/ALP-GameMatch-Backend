package com.main.Controller;

import com.main.Model.Favorite;
import com.main.Model.Game;
import com.main.Model.User;
import com.main.Repository.FavoriteRepo;
import com.main.Repository.GameRepo;
import com.main.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final UserRepo userRepo;
    private final GameRepo gameRepo;
    private final FavoriteRepo favoriteRepo;

    // Menarik daftar game favorit milik user (Dipanggil oleh halaman Favorite)
    @GetMapping("/{email}")
    public ResponseEntity<List<Game>> getUserFavorites(@PathVariable String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }

        // Ambil semua data Favorite milik user ini, lalu ekstrak hanya Game-nya saja
        List<Game> favGames = favoriteRepo.findAll().stream()
                .filter(fav -> fav.getUser().getUserId().equals(user.getUserId()))
                .map(Favorite::getGame)
                .collect(Collectors.toList());

        return ResponseEntity.ok(favGames);
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleFavorite(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        Integer gameId = ((Number) payload.get("gameId")).intValue();

        Game game = gameRepo.findById(gameId).orElse(null);
        if (game == null)
            return ResponseEntity.badRequest().body("Game tidak ditemukan");

        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            return userRepo.save(newUser);
        });

        // Cek apakah relasi favorit ini sudah ada
        Favorite existingFav = favoriteRepo.findAll().stream()
                .filter(f -> f.getUser().getUserId().equals(user.getUserId()) && f.getGame().getGameId().equals(gameId))
                .findFirst()
                .orElse(null);

        if (existingFav != null) {
            // Jika sudah ada, hapus
            favoriteRepo.delete(existingFav);
            return ResponseEntity.ok(Map.of("status", "removed"));
        } else {
            // Jika belum ada, buat baru
            Favorite newFav = new Favorite();
            newFav.setUser(user);
            newFav.setGame(game);
            newFav.setSavedAt(LocalDateTime.now());
            favoriteRepo.save(newFav);
            return ResponseEntity.ok(Map.of("status", "added"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFavorite(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        Integer gameId = ((Number) payload.get("gameId")).intValue();

        Game game = gameRepo.findById(gameId).orElse(null);
        if (game == null)
            return ResponseEntity.badRequest().body("Game tidak ditemukan");

        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null)
            return ResponseEntity.badRequest().body("User tidak ditemukan");

        Favorite existingFav = favoriteRepo.findAll().stream()
                .filter(f -> f.getUser().getUserId().equals(user.getUserId()) && f.getGame().getGameId().equals(gameId))
                .findFirst()
                .orElse(null);

        if (existingFav != null) {
            favoriteRepo.delete(existingFav);
            return ResponseEntity.ok(Map.of("status", "removed"));
        } else {
            return ResponseEntity.badRequest().body("Favorite tidak ditemukan");
        }
    }
}