package com.main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.Model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
