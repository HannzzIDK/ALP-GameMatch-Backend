package com.main.dto;

import java.util.List;

public class QuizSubmitRequestDTO {
    private Double maxBudget;
    private String targetTier;
    private List<AnswerDTO> answers;

    public QuizSubmitRequestDTO() {
    }

    public QuizSubmitRequestDTO(Double maxBudget, String targetTier, List<AnswerDTO> answers) {
        this.maxBudget = maxBudget;
        this.targetTier = targetTier;
        this.answers = answers;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public static class AnswerDTO {
        private Integer questionId;
        private Integer answerOptionId;

        public AnswerDTO() {
        }

        public AnswerDTO(Integer questionId, Integer answerOptionId) {
            this.questionId = questionId;
            this.answerOptionId = answerOptionId;
        }

        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public Integer getAnswerOptionId() {
            return answerOptionId;
        }

        public void setAnswerOptionId(Integer answerOptionId) {
            this.answerOptionId = answerOptionId;
        }
    }
}
