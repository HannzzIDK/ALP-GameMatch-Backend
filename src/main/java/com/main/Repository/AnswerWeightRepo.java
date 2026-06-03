package com.main.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.Model.AnswerWeight;

public interface AnswerWeightRepo extends JpaRepository <AnswerWeight, Integer> {
    
}
