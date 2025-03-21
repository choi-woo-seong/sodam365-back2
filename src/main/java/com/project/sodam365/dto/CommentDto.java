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
    private Long id;
    private String c_comment;
    private String authorName;
    private String authorType;
    private Long communityId;
    private LocalDateTime createdAt;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .c_comment(comment.getC_comment())
                .authorName(comment.getAuthorName())
                .authorType(comment.getAuthorType())
                .communityId(comment.getCommunity().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public Comment toEntity(User user, Nuser nuser, Community community) {
        return Comment.builder()
                .c_comment(this.c_comment)
                .authorName(user != null ? user.getName() : nuser.getNName())
                .authorType(user != null ? "USER" : "NUSER")
                .user(user)
                .nuser(nuser)
                .community(community)
                .build();
    }
}
