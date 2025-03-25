package com.project.sodam365.controller;

import com.project.sodam365.dto.FavoriteResponseDto;
import com.project.sodam365.dto.FavoriteToggleRequest;
import com.project.sodam365.entity.*;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.repository.UserRepository;
import com.project.sodam365.service.FavoriteService;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;

    /**
     * 찜 토글 API (JSON 요청 바디 기반)
     */
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleFavorite(
            @RequestHeader("Authorization") String token,
            @RequestBody FavoriteToggleRequest request
    ) {
        String userType = jwtUtil.extractUserType(token);
        String userId = jwtUtil.extractUserId(token);
        boolean result;

        
        if ("buser".equals(userType)) {
            User user = userRepository.findByUserid(userId)
                    .orElseThrow(() -> new RuntimeException("해당 user 없음"));
            result = favoriteService.toggleFavorite(user, null, request.getTargetId(), request.getTargetType());
        } else {
            Nuser nuser = nuserRepository.findByNUserid(userId)
                    .orElseThrow(() -> new RuntimeException("해당 nuser 없음"));
            result = favoriteService.toggleFavorite(null, nuser, request.getTargetId(), request.getTargetType());
        }



        return ResponseEntity.ok(result ? "찜 추가됨" : "찜 해제됨");
    }

    /**
     * 전체 찜 목록 조회 API
     */
    @GetMapping("/searchAll")
    public ResponseEntity<List<Favorite>> getFavoriteList(@RequestHeader("Authorization") String token) {
        String userType = jwtUtil.extractUserType(token);
        String userId = jwtUtil.extractUserId(token);

        List<Favorite> favoriteList;

        if ("buser".equals(userType)) {
            favoriteList = favoriteService.getFavoritesByUser(userId);
        } else {
            favoriteList = favoriteService.getFavoritesByNuser(userId);
        }

        return ResponseEntity.ok(favoriteList);
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @RequestHeader("Authorization") String token,
            @RequestBody FavoriteToggleRequest request
    ) {
        String userType = jwtUtil.extractUserType(token);
        String userId = jwtUtil.extractUserId(token);
        boolean result;

        if ("buser".equals(userType)) {
            result = favoriteService.isFavoritedByUser(userId, request.getTargetId(), request.getTargetType());
        } else {
            result = favoriteService.isFavoritedByNuser(userId, request.getTargetId(), request.getTargetType());
        }

        return ResponseEntity.ok(Map.of("favorited", result));
    }

    @GetMapping("/mapped")
    public ResponseEntity<List<FavoriteResponseDto>> getMappedFavorites(
            @RequestHeader("Authorization") String token
    ) {
        String userType = jwtUtil.extractUserType(token);
        String userId = jwtUtil.extractUserId(token);

        List<FavoriteResponseDto> response = favoriteService.getMappedFavorites(userType, userId);
        return ResponseEntity.ok(response);
    }



}
