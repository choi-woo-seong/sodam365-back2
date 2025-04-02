package com.project.sodam365.service;

import com.project.sodam365.dto.CommentDto;
import com.project.sodam365.entity.*;
import com.project.sodam365.exception.CommentNotFoundException;
import com.project.sodam365.exception.UnauthorizedAccessException;
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
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));

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
                .orElseThrow(() -> new CommentNotFoundException("댓글이 없습니다."));

        boolean isOwner = (comment.getUser() != null && comment.getUser().getUserid().equals(userId)) ||
                (comment.getNuser() != null && comment.getNuser().getNUserid().equals(userId));

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedAccessException("댓글 수정 권한이 없습니다.");
        }

        comment.setC_comment(dto.getC_comment());
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public void delete(Long commentId, String token) {
        String userId = jwtUtil.extractUsername(token);
        boolean isAdmin = jwtUtil.isAdmin(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글이 없습니다."));

        boolean isOwner = (comment.getUser() != null && comment.getUser().getUserid().equals(userId)) ||
                (comment.getNuser() != null && comment.getNuser().getNUserid().equals(userId));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("삭제 권한 없음");
        }

        commentRepository.delete(comment);
    }
}
