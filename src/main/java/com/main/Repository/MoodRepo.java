package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.Mood;
public interface MoodRepo extends JpaRepository<Mood, Long> {
    
}
