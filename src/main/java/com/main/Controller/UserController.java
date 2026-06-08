package com.main.Controller;

import com.main.Model.User;
import com.main.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/quiz-status/complete")
    public ResponseEntity<?> markQuizAsCompleted(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null)
            return ResponseEntity.badRequest().body("Email kosong");

        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setQuizCompleted(true); // Ubah jadi TRUE di sini
            userRepo.save(user);
            return ResponseEntity.ok(Map.of("message", "Status kuis diupdate"));
        }
        return ResponseEntity.notFound().build();
    }
}