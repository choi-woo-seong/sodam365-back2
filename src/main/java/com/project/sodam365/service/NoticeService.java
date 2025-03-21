package com.project.sodam365.service;

import com.project.sodam365.dto.NoticeDto;
import com.project.sodam365.entity.Notice;
import com.project.sodam365.repository.NoticeRepository;
import com.project.sodam365.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final JwtUtil jwtUtil; // JWT 토큰 검증

    // ✅ 공지사항 생성 (admin만 가능)
    public NoticeDto createNotice(NoticeDto dto, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다."); // ✅ admin 권한 확인
        }

        String username = jwtUtil.extractUsername(token); // 유저 ID 가져오기
        Notice notice = new Notice(dto.getN_title(), dto.getN_content(), username);
        noticeRepository.save(notice);
        return toDto(notice);
    }

    // ✅ 전체 공지사항 조회 (모든 사용자 가능)
    public List<NoticeDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ✅ 특정 공지사항 조회 (모든 사용자 가능)
    public NoticeDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        return toDto(notice);
    }

    // ✅ 공지사항 수정 (admin만 가능)
    @Transactional
    public NoticeDto updateNotice(Long id, NoticeDto dto, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다."); // ✅ admin 권한 확인
        }

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        notice.update(dto.getN_title(), dto.getN_content());
        return toDto(notice);
    }

    // ✅ 공지사항 삭제 (admin만 가능)
    public void deleteNotice(Long id, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다."); // ✅ admin 권한 확인
        }

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        noticeRepository.delete(notice);
    }

    // ✅ 엔티티 → DTO 변환
    private NoticeDto toDto(Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getN_title(),
                notice.getN_content(),
                notice.getUser(),
                notice.getCreatedAt()
        );
    }
}
