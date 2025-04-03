package com.project.sodam365.controller;

import com.project.sodam365.dto.MyPostsResponseDto;
import com.project.sodam365.service.MyPageService;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtUtil jwtUtil;

    // ✅ 내가 쓴 전체 글 조회 (상품, 사업, 커뮤니티, 질문)
    @GetMapping("/posts")
    public ResponseEntity<MyPostsResponseDto> getMyPosts(@RequestHeader("Authorization") String token) {
        // 사용자 타입(user, nuser, admin)과 ID 추출 (Bearer 제거 포함)
        String userType = jwtUtil.extractUserType(token); // "user" 또는 "nuser"
        String userId = jwtUtil.extractUserId(token);     // String ID

        return ResponseEntity.ok(myPageService.getMyPosts(userType, userId));
    }
}
