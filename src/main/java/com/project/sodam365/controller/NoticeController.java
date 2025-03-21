package com.project.sodam365.controller;

import com.project.sodam365.dto.NoticeDto;
import com.project.sodam365.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // ✅ 1. 공지사항 생성 (JWT 필요, admin만 가능)
    @PostMapping("/create")
    public ResponseEntity<NoticeDto> createNotice(
            @RequestBody NoticeDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(noticeService.createNotice(dto, token));
    }

    // ✅ 2. 전체 공지사항 조회 (모든 사용자 가능)
    @GetMapping("/searchAll")
    public ResponseEntity<List<NoticeDto>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    // ✅ 3. 특정 공지사항 조회 (모든 사용자 가능)
    @GetMapping("/noticeDetail/{id}")
    public ResponseEntity<NoticeDto> getNoticeById(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }

    // ✅ 4. 공지사항 수정 (JWT 인증 필요, admin만 가능)
    @PutMapping("/update/{id}")
    public ResponseEntity<NoticeDto> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(noticeService.updateNotice(id, dto, token));
    }

    // ✅ 5. 공지사항 삭제 (JWT 인증 필요, admin만 가능)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        noticeService.deleteNotice(id, token);
        return ResponseEntity.ok().build();
    }
}
