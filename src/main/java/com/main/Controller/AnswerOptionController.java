package com.main.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.main.Model.AnswerOption;
import com.main.Repository.AnswerOptionRepo;

@Controller
@RequestMapping(path = "/answerOptions")
public class AnswerOptionController {

    @Autowired
    private AnswerOptionRepo repo;

}
