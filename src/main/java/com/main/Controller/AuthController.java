package com.main.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.main.service.AuthService;
import com.main.Repository.UserRepo;
import com.main.Model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleAuthRequestDTO request) {
        String token = authService.authenticate(request.getIdToken());
        String email = request.getEmail();

        User currentUser = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setQuizCompleted(false); // Secara default, user baru belum kuis
            return userRepo.save(newUser);
        });

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("email", currentUser.getEmail());
        responseData.put("isQuizCompleted", currentUser.isQuizCompleted());

        return ResponseEntity.ok(responseData);
    }

    public static class GoogleAuthRequestDTO {
        private String idToken;
        private String email;

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}