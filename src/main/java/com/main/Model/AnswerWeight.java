package com.main.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "answer_weight")
public class AnswerWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weightId;

    @ManyToOne
    @JoinColumn(name = "answer_option_id")
    private AnswerOption answerOption;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    private Double weightScore;

    public Integer getWeightId() {
        return weightId;
    }

    public void setWeightId(Integer weightId) {
        this.weightId = weightId;
    }

    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Double getWeightScore() {
        return weightScore;
    }

    public void setWeightScore(Double weightScore) {
        this.weightScore = weightScore;
    }
}