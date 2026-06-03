package com.main.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepo extends CrudRepository <Question, Long> {
    
}
