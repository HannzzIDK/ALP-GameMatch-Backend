package com.main.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.main.Model.Question;
import com.main.Model.QuizResult;
import com.main.Model.QuizResultAnswer;
import com.main.Model.User;
import com.main.Model.AnswerOption;
import com.main.Repository.QuestionRepo;
import com.main.Repository.QuizResultRepo;
import com.main.Repository.QuizResultAnswerRepo;
import com.main.Repository.UserRepo;
import com.main.Repository.AnswerOptionRepo;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuizResultRepo quizResultRepo;

    @Autowired
    private QuizResultAnswerRepo quizResultAnswerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AnswerOptionRepo answerOptionRepo;

    @GetMapping("/questions")
    public Iterable<Question> getAllQuestion() {
        return questionRepo.findAll();
    }

    public static class QuizSubmission {
        private Integer userId;
        private List<AnswerSubmission> answers;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public List<AnswerSubmission> getAnswers() {
            return answers;
        }

        public void setAnswers(List<AnswerSubmission> answers) {
            this.answers = answers;
        }
    }

    public static class AnswerSubmission {
        private Integer questionId;
        private Integer answerOptionId;

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

    @PostMapping("/submit")
    public String submitQuiz(@RequestBody QuizSubmission submission) {
        User user = userRepo.findById(Long.valueOf(submission.getUserId())).orElse(null);
        if (user == null) {
            return "User not found";
        }

        QuizResult result = new QuizResult();
        result.setUser(user);
        result.setCompletedAt(LocalDateTime.now());
        result.setTotalScore(0.0);
        quizResultRepo.save(result);

        if (submission.getAnswers() != null) {
            for (AnswerSubmission ans : submission.getAnswers()) {
                Question question = questionRepo.findById(Long.valueOf(ans.getQuestionId())).orElse(null);
                AnswerOption option = answerOptionRepo.findById(ans.getAnswerOptionId()).orElse(null);
                if (question != null && option != null) {
                    QuizResultAnswer qra = new QuizResultAnswer();
                    qra.setQuizResult(result);
                    qra.setQuestion(question);
                    qra.setAnswerOption(option);
                    quizResultAnswerRepo.save(qra);
                }
            }
        }
        return "Saved";
    }
}