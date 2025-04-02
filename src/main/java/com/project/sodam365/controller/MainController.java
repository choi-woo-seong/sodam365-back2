package com.project.sodam365.controller;

import com.project.sodam365.dto.*;
import com.project.sodam365.entity.Notice;
import com.project.sodam365.entity.Recent;
import com.project.sodam365.repository.*;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final ProductRepository productRepository;
    private final BizRepository bizRepository;
    private final GovRepository govRepository;
    private final CommunityRepository communityRepository;
    private final QuestionRepository questionRepository;
    private final RecentRepository recentRepository;
    private final NoticeRepository noticeRepository;
    private final JwtUtil jwtUtil;

    // ✅ 메인 페이지 데이터 조회 (각 카테고리별 최근 3개 + 최근 본 글)
    @GetMapping("/recent-posts")
    public ResponseEntity<Map<String, List<Object>>> getRecentPosts(
            @RequestHeader("Authorization") String token) {

        // ✅ JWT에서 userid 추출
        String userid = jwtUtil.extractUsername(token);

        Map<String, List<Object>> recentPosts = new HashMap<>();

        // ✅ 각 엔티티 데이터를 DTO로 변환 후 저장 (중복된 코드 제거)
        recentPosts.put("product", productRepository.findTop3ByOrderByNoDesc()
                .stream().map(ProductDto::fromEntity).collect(Collectors.toList()));

        recentPosts.put("biz", bizRepository.findTop3ByOrderByNoDesc()
                .stream().map(BizDto::fromEntity).collect(Collectors.toList()));

        recentPosts.put("gov", govRepository.findTop3ByOrderByNoDesc()
                .stream().map(GovDto::fromEntity).collect(Collectors.toList()));

        recentPosts.put("community", communityRepository.findTop3ByOrderByIdDesc()
                .stream().map(CommunityDto::fromEntity).collect(Collectors.toList()));

        recentPosts.put("question", questionRepository.findTop3ByOrderByIdDesc()
                .stream().map(QuestionDto::fromEntity).collect(Collectors.toList()));

        recentPosts.put("notice", noticeRepository.findTop3ByOrderByIdDesc()
                .stream().map(NoticeDto::fromEntity).collect(Collectors.toList()));

        // ✅ 최근 본 글 조회 (DTO 변환 후 저장)
        recentPosts.put("recent", recentRepository.findTop5ByUseridOrderByViewedAtDesc(userid)
                .stream().map(RecentDto::fromEntity).collect(Collectors.toList()));

        return ResponseEntity.ok(recentPosts);
    }

    // ✅ 특정 게시글을 볼 때 "최근 본 글"로 저장
    @PostMapping("/save-recent")
    public ResponseEntity<Void> saveRecentPost(
            @RequestHeader("Authorization") String token,
            @RequestBody RecentDto recentDto) {

        // ✅ JWT에서 userid 추출
        String userid = jwtUtil.extractUsername(token);

        // ✅ 최근 본 글 저장
        saveRecent(userid, recentDto);

        return ResponseEntity.ok().build();
    }

    // ✅ 최근 본 글 저장 메서드 (중복 방지)
    private void saveRecent(String userid, RecentDto recentDto) {
        // 기존 데이터 삭제 (중복 방지)
        recentRepository.deleteByUseridAndPostNo(userid, recentDto.getPostNo());

        // 새로운 데이터 저장
        Recent recent = Recent.builder()
                .postNo(recentDto.getPostNo())
                .category(recentDto.getCategory())
                .title(recentDto.getTitle())  // ✅ 필드명 일치 수정
                .userid(userid)
                .viewedAt(LocalDateTime.now())
                .build();

        recentRepository.save(recent);
    }
}
