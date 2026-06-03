package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.Question;
public interface QuestionRepo extends JpaRepository <Question, Long> {
    
}
