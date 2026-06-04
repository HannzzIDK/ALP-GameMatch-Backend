package com.main.service;

import com.main.Model.AnswerOption;
import com.main.Model.Question;
import com.main.Model.QuizResult;
import com.main.Model.QuizResultAnswer;
import com.main.Model.User;
import com.main.Repository.AnswerOptionRepo;
import com.main.Repository.QuestionRepo;
import com.main.Repository.QuizResultAnswerRepo;
import com.main.Repository.QuizResultRepo;
import com.main.Repository.UserRepo;
import com.main.dto.QuizSubmitRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuestionRepo questionRepo;
    private final AnswerOptionRepo answerOptionRepo;
    private final QuizResultRepo quizResultRepo;
    private final QuizResultAnswerRepo quizResultAnswerRepo;
    private final UserRepo userRepo;
    private final MatchmakerService matchmakerService;

    public Iterable<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public List<com.main.dto.GameResponseDTO> submitQuiz(Long userId, QuizSubmitRequestDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        QuizResult quizResult = new QuizResult();
        quizResult.setUser(user);
        quizResult.setCompletedAt(LocalDateTime.now());
        quizResult.setTotalScore(0.0);
        quizResult = quizResultRepo.save(quizResult);

        int savedCount = 0;
        if (dto.getAnswers() != null) {
            for (QuizSubmitRequestDTO.AnswerDTO answerDTO : dto.getAnswers()) {
                Question question = questionRepo
                        .findById(Long.valueOf(answerDTO.getQuestionId()))
                        .orElse(null);
                AnswerOption option = answerOptionRepo
                        .findById(answerDTO.getAnswerOptionId())
                        .orElse(null);

                if (question != null && option != null) {
                    QuizResultAnswer qra = new QuizResultAnswer();
                    qra.setQuizResult(quizResult);
                    qra.setQuestion(question);
                    qra.setAnswerOption(option);
                    quizResultAnswerRepo.save(qra);
                    savedCount++;
                }
            }
        }

        quizResult.setTotalScore((double) savedCount);
        quizResultRepo.save(quizResult);

        return matchmakerService.generateRecommendation(dto);
    }
}
