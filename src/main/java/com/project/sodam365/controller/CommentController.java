package com.project.sodam365.controller;

import com.project.sodam365.dto.CommentDto;
import com.project.sodam365.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentDto> create(@RequestBody CommentDto dto,
                                             @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.create(dto, token));
    }

    @GetMapping("/byCommunity/{communityId}")
    public ResponseEntity<List<CommentDto>> getByCommunity(@PathVariable Long communityId) {
        return ResponseEntity.ok(commentService.getByCommunity(communityId));
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long commentId,
                                             @RequestBody CommentDto dto,
                                             @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.update(commentId, dto, token));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId,
                                       @RequestHeader("Authorization") String token) {
        commentService.delete(commentId, token);
        return ResponseEntity.ok().build();
    }
}
