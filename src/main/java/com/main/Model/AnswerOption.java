package com.main.Answer;
import com.main.Question.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "answer_options")
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerOptionId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private String answerText;
    private Integer answerOrder;

    public Integer getAnswerOptionId() {
        return answerOptionId;
    }
    public void setAnswerOptionId(Integer answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getAnswerOrder() {
        return answerOrder;
    }
    public void setAnswerOrder(Integer answerOrder) {
        this.answerOrder = answerOrder;
    }
}
