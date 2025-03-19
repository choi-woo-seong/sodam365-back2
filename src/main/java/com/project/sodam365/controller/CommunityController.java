package com.project.sodam365.controller;

import com.project.sodam365.dto.CommunityDto;
import com.project.sodam365.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // ✅ 1. 게시글 생성 (JWT 필요)
    @PostMapping("/create")
    public ResponseEntity<CommunityDto> createPost(
            @RequestBody CommunityDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(communityService.createCommunityPost(dto, token));
    }

    // ✅ 2. 전체 게시글 조회
    @GetMapping("/searchAll")
    public ResponseEntity<List<CommunityDto>> getAllPosts() {
        return ResponseEntity.ok(communityService.getAllPosts());
    }

    // ✅ 3. 특정 게시글 조회
    @GetMapping("/communityDetail/{id}")
    public ResponseEntity<CommunityDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(communityService.getPostById(id));
    }

    // ✅ 4. 게시글 수정 (JWT 인증 필요)
    @PutMapping("/update/{id}")
    public ResponseEntity<CommunityDto> updatePost(
            @PathVariable Long id,
            @RequestBody CommunityDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(communityService.updatePost(id, dto, token));
    }

    // ✅ 5. 게시글 삭제 (JWT 인증 필요)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        communityService.deletePost(id, token);
        return ResponseEntity.ok().build();
    }
}
