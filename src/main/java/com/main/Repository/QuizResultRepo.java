package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.QuizResult;
public interface QuizResultRepo extends JpaRepository <QuizResult, Long> {
    
}
