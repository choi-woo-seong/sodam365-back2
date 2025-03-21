package com.project.sodam365.dto;

import com.project.sodam365.entity.Community;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections; // ✅ 추가 필요


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityDto {
    private Long id; // 게시글 ID
    private String c_title;  // ✅ 게시글 제목
    private String c_content; // ✅ 게시글 내용
    private String authorName; // ✅ 작성자 이름 (User 또는 Nuser)
    private String authorType; // ✅ 작성자 유형 ("USER" 또는 "NUSER")
    private List<CommentDto> comments; // ✅ 댓글 리스트 추가
    private LocalDateTime createdAt; // ✅ 작성시간 추가



    // ✅ Entity → DTO 변환
    public static CommunityDto fromEntity(Community community) {
        return CommunityDto.builder()
                .id(community.getId())
                .c_title(community.getC_title())
                .c_content(community.getC_content())
                .authorName(community.getAuthorName())
                .authorType(community.getAuthorType())
                .createdAt(community.getCreatedAt()) // ✅ 추가
                .comments(community.getComments() != null ?
                        community.getComments().stream().map(CommentDto::fromEntity).collect(Collectors.toList())
                        : Collections.emptyList()) // ✅ `null`이면 빈 리스트 반환
                .build();
    }

    // ✅ DTO → Entity 변환
    public static Community toEntity(CommunityDto dto) {
        return Community.builder()
                .id(dto.getId())
                .c_title(dto.getC_title()) // ✅ 필드명 변경
                .c_content(dto.getC_content()) // ✅ 필드명 변경
                .authorName(dto.getAuthorName())
                .authorType(dto.getAuthorType())
                .build();
    }
}
