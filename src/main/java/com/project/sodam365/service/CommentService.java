package com.project.sodam365.service;

import com.project.sodam365.dto.CommentDto;
import com.project.sodam365.entity.*;
import com.project.sodam365.repository.*;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;
    private final JwtUtil jwtUtil;

    public CommentDto create(CommentDto dto, String token) {
        String userId = jwtUtil.extractUsername(token);
        User user = userRepository.findById(userId).orElse(null);
        Nuser nuser = nuserRepository.findById(userId).orElse(null);
        Community community = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Comment comment = dto.toEntity(user, nuser, community);
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public List<CommentDto> getByCommunity(Long communityId) {
        return commentRepository.findByCommunityId(communityId)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CommentDto update(Long commentId, CommentDto dto, String token) {
        String userId = jwtUtil.extractUsername(token);
        boolean isAdmin = jwtUtil.isAdmin(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getAuthorName().equals(userId) && !isAdmin)
            throw new RuntimeException("수정 권한 없음");

        comment.setC_comment(dto.getC_comment());
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public void delete(Long commentId, String token) {
        String userId = jwtUtil.extractUsername(token);
        boolean isAdmin = jwtUtil.isAdmin(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getAuthorName().equals(userId) && !isAdmin)
            throw new RuntimeException("삭제 권한 없음");

        commentRepository.delete(comment);
    }
}
