package com.project.sodam365.dto;

import com.project.sodam365.entity.Comment;
import com.project.sodam365.entity.Community;
import com.project.sodam365.entity.Nuser;
import com.project.sodam365.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id; // 댓글 ID
    private String c_comment; // ✅ 댓글 내용
    private String authorName; // ✅ 작성자 이름
    private String authorType; // ✅ 작성자 유형 ("USER" 또는 "NUSER")
    private Long communityId; // ✅ 게시글 ID
    private LocalDateTime createdAt; // ✅ 작성시간 추가


    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .c_comment(comment.getC_comment())
                .authorName(comment.getAuthorName())
                .authorType(comment.getAuthorType())
                .createdAt(comment.getCreatedAt()) // ✅ 추가
                .communityId(comment.getCommunity().getId())
                .build();
    }

    public static Comment toEntity(CommentDto dto, Community community, User user, Nuser nuser) {
        return Comment.builder()
                .c_comment(dto.getC_comment())
                .authorName(user != null ? user.getName() : nuser.getNName())
                .authorType(user != null ? "USER" : "NUSER")
                .community(community)
                .user(user)
                .nuser(nuser)
                .build();
    }
}
