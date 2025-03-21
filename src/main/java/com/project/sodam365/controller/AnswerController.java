package com.project.sodam365.controller;

import com.project.sodam365.dto.AnswerDto;
import com.project.sodam365.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AnswerDto dto,
                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(answerService.createAnswer(dto, token));
    }

    @GetMapping("/searchAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/answerDetail/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    @GetMapping("/byQuestion/{questionId}")
    public ResponseEntity<?> getByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.getAnswerByQuestionId(questionId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody AnswerDto dto,
                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(answerService.updateAnswer(id, dto, token));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String token) {
        answerService.deleteAnswer(id, token);
        return ResponseEntity.ok().build();
    }
}
