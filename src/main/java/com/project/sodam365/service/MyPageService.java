package com.project.sodam365.service;

import com.project.sodam365.dto.*;
import com.project.sodam365.repository.BizRepository;
import com.project.sodam365.repository.CommunityRepository;
import com.project.sodam365.repository.ProductRepository;
import com.project.sodam365.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ProductRepository productRepository;
    private final BizRepository bizRepository;
    private final CommunityRepository communityRepository;
    private final QuestionRepository questionRepository;

    /**
     * 마이페이지 - 내가 쓴 모든 글 조회
     *
     * @param userType "user" or "nuser"
     * @param userId   사용자 ID (String)
     * @return 내가 쓴 글들을 담은 DTO
     */
    public MyPostsResponseDto getMyPosts(String userType, String userId) {

        // 초기화
        List<ProductDto> productDtos = new ArrayList<>();
        List<BizDto> bizDtos = new ArrayList<>();
        List<CommunityDto> communityDtos;
        List<QuestionDto> questionDtos;

        // ✅ 사업자 사용자인 경우
        if (userType.equals("buser")) {
            productDtos = productRepository.findByUser_Userid(userId)
                    .stream()
                    .map(ProductDto::fromEntity)
                    .collect(Collectors.toList());

            bizDtos = bizRepository.findByUserid_Userid(userId)
                    .stream()
                    .map(BizDto::fromEntity)
                    .collect(Collectors.toList());

            communityDtos = communityRepository.findByUser_Userid(userId)
                    .stream()
                    .map(CommunityDto::fromEntity)
                    .collect(Collectors.toList());

            questionDtos = questionRepository.findByUser_Userid(userId)
                    .stream()
                    .map(QuestionDto::fromEntity)
                    .collect(Collectors.toList());
        }
        // ✅ 일반 사용자 (nuser)인 경우
        else {
            communityDtos = communityRepository.findByNuser_nUserid(userId)
                    .stream()
                    .map(CommunityDto::fromEntity)
                    .collect(Collectors.toList());

            questionDtos = questionRepository.findByNuser_nUserid(userId)
                    .stream()
                    .map(QuestionDto::fromEntity)
                    .collect(Collectors.toList());
        }

        // DTO에 담아서 반환
        return new MyPostsResponseDto(productDtos, bizDtos, communityDtos, questionDtos, userType);

    }
}

