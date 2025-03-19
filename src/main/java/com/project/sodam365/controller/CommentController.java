package com.project.sodam365.controller;

import com.project.sodam365.dto.CommentDto;
import com.project.sodam365.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // ✅ 댓글 작성 (여러 개 가능)
    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.createComment(dto, token));
    }

    // ✅ 특정 게시글의 댓글 조회 (여러 개 가능)
    @GetMapping("/byCommunity/{communityId}")
    public ResponseEntity<List<CommentDto>> getCommentsByCommunityId(@PathVariable Long communityId) {
        return ResponseEntity.ok(commentService.getCommentsByCommunityId(communityId));
    }

    // ✅ 댓글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.updateComment(id, dto, token));
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        commentService.deleteComment(id, token);
        return ResponseEntity.ok().build();
    }
}
