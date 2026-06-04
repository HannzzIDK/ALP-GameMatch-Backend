package com.main.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerWeightRepo extends JpaRepository<com.main.Model.AnswerWeight, Integer> {

    @Query("SELECT aw FROM AnswerWeight aw WHERE aw.answerOption.answerOptionId IN :optionIds")
    List<com.main.Model.AnswerWeight> findByAnswerOptionIds(@Param("optionIds") List<Integer> optionIds);
}
