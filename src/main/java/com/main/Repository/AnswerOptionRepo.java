package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.AnswerOption;

public interface AnswerOptionRepo extends JpaRepository <AnswerOption, Integer> {
    
}