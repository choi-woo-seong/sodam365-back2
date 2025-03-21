package com.project.sodam365.entity;

import com.project.sodam365.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID (자동 증가)

    @Column(length = 500, nullable = false)
    private String c_comment; // ✅ 댓글 내용

    @Column(length = 50, nullable = false)
    private String authorName; // ✅ 작성자 이름 (User 또는 Nuser)

    @Column(length = 10, nullable = false)
    private String authorType; // ✅ "USER" 또는 "NUSER"

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community; // ✅ 댓글이 달린 게시글

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user; // ✅ User 테이블과 연결

    @ManyToOne
    @JoinColumn(name = "nuserid")
    private Nuser nuser; // ✅ Nuser 테이블과 연결

    @Column(updatable = false)
    private LocalDateTime createdAt; // ✅ nullable 제거 및 기본값 설정

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) { // ✅ createdAt이 null이면 현재 시간으로 설정
            createdAt = LocalDateTime.now();
        }
    }

    // ✅ `CommentDto` → `Comment` 변환하는 `toEntity` 메서드 추가
    public static Comment toEntity(CommentDto dto, Community community, User user, Nuser nuser) {
        return Comment.builder()
                .c_comment(dto.getC_comment()) // 댓글 내용 설정
                .authorName(user != null ? user.getName() : nuser.getNName()) // 작성자 이름 설정
                .authorType(user != null ? "USER" : "NUSER") // 작성자 유형 설정
                .community(community) // 댓글이 속한 게시글 설정
                .user(user) // User 정보 저장 (User가 작성한 경우)
                .nuser(nuser) // Nuser 정보 저장 (Nuser가 작성한 경우)
                .build();
    }
}
