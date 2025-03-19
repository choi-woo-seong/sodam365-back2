package com.project.sodam365.service;

import com.project.sodam365.dto.CommentDto;
import com.project.sodam365.entity.Comment;
import com.project.sodam365.entity.Community;
import com.project.sodam365.entity.User;
import com.project.sodam365.entity.Nuser;
import com.project.sodam365.repository.CommentRepository;
import com.project.sodam365.repository.CommunityRepository;
import com.project.sodam365.repository.UserRepository;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;
    private final JwtUtil jwtUtil;

    // ✅ 댓글 작성 (여러 개 가능)
    public CommentDto createComment(CommentDto dto, String token) {
        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        User user = userRepository.findByUserid(userid).orElse(null);
        Nuser nuser = nuserRepository.findByNUserid(userid).orElse(null);

        if (user == null && nuser == null) {
            throw new EntityNotFoundException("유효한 사용자 정보가 없습니다.");
        }

        Community community = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.toEntity(dto, community, user, nuser);
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // ✅ 특정 게시글의 댓글 목록 조회 (여러 개 가능)
    public List<CommentDto> getCommentsByCommunityId(Long communityId) {
        return commentRepository.findByCommunityId(communityId).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 댓글 수정 (JWT 필요)
    public CommentDto updateComment(Long id, CommentDto dto, String token) {
        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        // ✅ 작성자 검증
        if (!(comment.getUser() != null && comment.getUser().getUserid().equals(userid)) &&
                !(comment.getNuser() != null && comment.getNuser().getNUserid().equals(userid))) {
            throw new IllegalStateException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setC_comment(dto.getC_comment());
        return CommentDto.fromEntity(comment);
    }

    // ✅ 댓글 삭제 (JWT 필요)
    public void deleteComment(Long id, String token) {
        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        // ✅ 작성자 검증
        if (!(comment.getUser() != null && comment.getUser().getUserid().equals(userid)) &&
                !(comment.getNuser() != null && comment.getNuser().getNUserid().equals(userid))) {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
