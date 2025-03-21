package com.project.sodam365.controller;

import com.project.sodam365.dto.QuestionDto;
import com.project.sodam365.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody QuestionDto dto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(questionService.createQuestion(dto, token));
    }

    @GetMapping("/searchAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/questionDetail/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id,
                                            @RequestBody QuestionDto dto,
                                            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto, token));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        questionService.deleteQuestion(id, token);
        return ResponseEntity.ok().build();
    }
}