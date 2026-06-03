package com.main.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.main.Model.AnswerOption;
import com.main.Model.Question;
import com.main.Repository.AnswerOptionRepo;
import com.main.Repository.QuestionRepo;

@Controller
@RequestMapping(path = "/answerOptions")
public class AnswerOptionController {

    @Autowired
    private AnswerOptionRepo repo;

    @Autowired
    private QuestionRepo questionRepo;

    @GetMapping("/")
    public @ResponseBody Iterable<AnswerOption> getAllAnswerOption() {
        return repo.findAll();
    }

    @PostMapping("/")
    public @ResponseBody String addAnswerOption(@RequestParam Long questionId, @RequestParam String answerText, @RequestParam Integer answerOrder) {
        Question question = questionRepo.findById(questionId).orElse(null);
        if (question == null) {
            return "Question not found";
        }
        AnswerOption answerOption = new AnswerOption();
        answerOption.setQuestion(question);
        answerOption.setAnswerText(answerText);
        answerOption.setAnswerOrder(answerOrder);
        repo.save(answerOption);
        return "Saved";
    }

    @PutMapping("/")
    public @ResponseBody String updateAnswerOption(@RequestParam Integer id, @RequestParam Long questionId, @RequestParam String answerText, @RequestParam Integer answerOrder) {
        AnswerOption answerOption = repo.findById(id).orElse(null);
        if (answerOption == null) {
            return "AnswerOption not found";
        }
        Question question = questionRepo.findById(questionId).orElse(null);
        if (question == null) {
            return "Question not found";
        }
        answerOption.setQuestion(question);
        answerOption.setAnswerText(answerText);
        answerOption.setAnswerOrder(answerOrder);
        repo.save(answerOption);
        return "Updated";
    }
}
