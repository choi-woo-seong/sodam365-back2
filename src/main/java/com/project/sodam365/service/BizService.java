package com.project.sodam365.service;

import com.project.sodam365.dto.BizDto;
import com.project.sodam365.entity.Biz;
import com.project.sodam365.entity.FavoriteType;
import com.project.sodam365.entity.User;
import com.project.sodam365.repository.BizRepository;
import com.project.sodam365.repository.FavoriteRepository;
import com.project.sodam365.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BizService {

    private final BizRepository bizRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    // 게시글 등록
    public BizDto createBiz(BizDto bizDto) {
        User user = userRepository.findByUserid(bizDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Biz biz = BizDto.toEntity(bizDto, user);
        Biz saved = bizRepository.save(biz); // ✅ 저장 후 반환된 객체로 DTO 변환
        return BizDto.fromEntity(saved);
    }

    // 전체 게시글 조회
    public List<BizDto> getAllBiz() {
        return bizRepository.findAll().stream()
                .map(BizDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 게시글 조회
    public BizDto getBizByNo(Long no) {
        Biz biz = bizRepository.findById(no)
                .orElseThrow(() -> new EntityNotFoundException("비즈니스 게시글을 찾을 수 없습니다: " + no));
        return BizDto.fromEntity(biz);
    }

    // 게시글 수정
    public BizDto updateBiz(Long no, BizDto bizDto, String userid) {
        Biz biz = bizRepository.findById(no)
                .orElseThrow(() -> new EntityNotFoundException("비즈니스 게시글을 찾을 수 없습니다: " + no));

        if (!biz.getUserid().getUserid().equals(userid)) {
            throw new IllegalStateException("이 게시글을 수정할 권한이 없습니다.");
        }

        biz.setB_title(bizDto.getB_title());
        biz.setB_contents(bizDto.getB_contents());
        biz.setB_link(bizDto.getB_link());
        biz.setB_price(bizDto.getB_price());

        return BizDto.fromEntity(bizRepository.save(biz));
    }

    // 게시글 삭제
    public void deleteBiz(Long no, String userid) {
        Biz biz = bizRepository.findById(no)
                .orElseThrow(() -> new EntityNotFoundException("비즈니스 게시글을 찾을 수 없습니다: " + no));

        if (!biz.getUserid().getUserid().equals(userid)) {
            throw new IllegalStateException("이 게시글을 삭제할 권한이 없습니다.");
        }

        favoriteRepository.deleteByTargetIdAndTargetType(no, FavoriteType.BIZ); // 🧹 찜 먼저 삭제
        bizRepository.delete(biz);
    }
}