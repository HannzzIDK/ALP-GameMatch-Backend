package com.main.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.main.Model.Mood;
import com.main.Model.Genre;
import com.main.Repository.MoodRepo;
import com.main.Repository.GenreRepo;

@RestController
@RequestMapping("/reference")
public class ReferenceController {
    
    @Autowired
    private MoodRepo moodRepo;

    @Autowired
    private GenreRepo genreRepo;
    
    @GetMapping("/moods")
    public Iterable<Mood> getAllMood() {
        return moodRepo.findAll();
    }
    
    @GetMapping("/genres")
    public Iterable<Genre> getAllGenre() {
        return genreRepo.findAll();
    }
}