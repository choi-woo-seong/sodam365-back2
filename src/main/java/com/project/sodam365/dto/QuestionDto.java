package com.project.sodam365.dto;

import com.project.sodam365.entity.Question;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private Long id;
    private String title;
    private String content;
    private boolean isAnswered;
    private String writer;       // userID or nuserID
    private String writerType;   // "user" or "nuser"
    private String writerName;   // 이름 추가
    private LocalDateTime createdAt;

    public static QuestionDto fromEntity(Question question) {
        String writerId = null;
        String writerType = null;
        String writerName = null;

        if (question.getUser() != null) {
            writerId = question.getUser().getUserid();
            writerType = "user";
            writerName = question.getUser().getName(); // User 이름
        } else if (question.getNuser() != null) {
            writerId = question.getNuser().getNUserid();
            writerType = "nuser";
            writerName = question.getNuser().getNName(); // Nuser 이름
        }

        return QuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .isAnswered(question.isAnswered())
                .writer(writerId)
                .writerType(writerType)
                .writerName(writerName)
                .createdAt(question.getCreatedAt())
                .build();
    }
}
