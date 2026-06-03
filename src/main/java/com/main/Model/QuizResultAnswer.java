package com.main.Quiz;
import com.main.Answer.AnswerOption;
import com.main.Question.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quiz_result_answers")
public class QuizResultAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quizResultAnswerId;
    
    @ManyToOne
    @JoinColumn(name = "quiz_result_id")
    private QuizResult quizResult;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_option_id")
    private AnswerOption answerOption;

    public Integer getQuizResultAnswerId() {
        return quizResultAnswerId;
    }
    public void setQuizResultAnswerId(Integer quizResultAnswerId) {
        this.quizResultAnswerId = quizResultAnswerId;
    }

    public QuizResult getQuizResult() {
        return quizResult;
    }
    public void setQuizResult(QuizResult quizResult) {
        this.quizResult = quizResult;
    }

    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    public AnswerOption getAnswerOption() {
        return answerOption;
    }
    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }
}