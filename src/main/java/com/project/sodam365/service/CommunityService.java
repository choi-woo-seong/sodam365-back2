package com.project.sodam365.service;

import com.project.sodam365.dto.CommunityDto;
import com.project.sodam365.entity.Community;
import com.project.sodam365.entity.User;
import com.project.sodam365.entity.Nuser;
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
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;
    private final JwtUtil jwtUtil;

    // ✅ 1. 게시글 작성 (JWT 인증 기반)
    public CommunityDto createCommunityPost(CommunityDto dto, String token) {
        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        User user = userRepository.findByUserid(userid).orElse(null);
        Nuser nuser = nuserRepository.findByNUserid(userid).orElse(null);

        if (user == null && nuser == null) {
            throw new EntityNotFoundException("유효한 사용자 정보가 없습니다.");
        }

        Community community = Community.builder()
                .c_title(dto.getC_title())
                .c_content(dto.getC_content())
                .authorName(user != null ? user.getName() : nuser.getNName())
                .authorType(user != null ? "USER" : "NUSER")
                .user(user)
                .nuser(nuser)
                .build();

        communityRepository.save(community);
        return CommunityDto.fromEntity(community);
    }

    // ✅ 2. 전체 게시글 조회
    public List<CommunityDto> getAllPosts() {
        return communityRepository.findAll().stream()
                .map(CommunityDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 3. 특정 게시글 조회
    public CommunityDto getPostById(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        return CommunityDto.fromEntity(community);
    }

    // ✅ 4. 게시글 수정 (JWT 인증 필요)
    public CommunityDto updatePost(Long id, CommunityDto dto, String token) {
        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // ✅ 현재 로그인한 사용자가 작성자인지 확인
        if (!(community.getUser() != null && community.getUser().getUserid().equals(userid)) &&
                !(community.getNuser() != null && community.getNuser().getNUserid().equals(userid))) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        community.setC_title(dto.getC_title());
        community.setC_content(dto.getC_content());

        return CommunityDto.fromEntity(community);
    }

    // ✅ 5. 게시글 삭제 (JWT 인증 필요)
    public void deletePost(Long id, String token) {
        String userid = jwtUtil.extractUsername(token.replace("Bearer ", ""));

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // ✅ 현재 로그인한 사용자가 작성자인지 확인
        if (!(community.getUser() != null && community.getUser().getUserid().equals(userid)) &&
                !(community.getNuser() != null && community.getNuser().getNUserid().equals(userid))) {
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");
        }

        communityRepository.delete(community);
    }
}
