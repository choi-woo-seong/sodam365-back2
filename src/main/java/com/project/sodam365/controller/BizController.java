package com.project.sodam365.controller;

import com.project.sodam365.dto.BizDto;
import com.project.sodam365.service.BizService;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biz")
@RequiredArgsConstructor
public class BizController {

    private final BizService bizService;
    private final JwtUtil jwtUtil;

    // ✅ 비즈니스 게시글 등록 (JWT 인증 필요)
    @PostMapping("/create")
    public ResponseEntity<BizDto> createBiz(
            @RequestBody BizDto bizDto,
            @RequestHeader("Authorization") String token) {

        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        bizDto.setUserid(userid); // ✅ JWT에서 추출한 userid 설정

        return ResponseEntity.ok(bizService.createBiz(bizDto));
    }

    // ✅ 전체 게시글 조회
    @GetMapping("/searchAll")
    public ResponseEntity<List<BizDto>> getAllBiz() {
        return ResponseEntity.ok(bizService.getAllBiz());
    }

    // ✅ 특정 게시글 조회
    @GetMapping("/businessDetail/{no}")
    public ResponseEntity<BizDto> getBizByNo(@PathVariable Long no) {
        return ResponseEntity.ok(bizService.getBizByNo(no));
    }

    // ✅ 게시글 수정 (JWT 인증 필요, 등록한 사용자만 수정 가능)
    @PutMapping("/update/{no}")
    public ResponseEntity<BizDto> updateBiz(
            @PathVariable Long no,
            @RequestBody BizDto bizDto,
            @RequestHeader("Authorization") String token) {

        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return ResponseEntity.ok(bizService.updateBiz(no, bizDto, userid));
    }

    // ✅ 게시글 삭제 (JWT 인증 필요, 등록한 사용자만 삭제 가능)
    @DeleteMapping("/delete/{no}")
    public ResponseEntity<Void> deleteBiz(
            @PathVariable Long no,
            @RequestHeader("Authorization") String token) {

        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        bizService.deleteBiz(no, userid);
        return ResponseEntity.noContent().build();
    }
}
