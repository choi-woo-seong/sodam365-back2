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
    private String writer; // userID or nuserID
    private String writerType; // "user" or "nuser"
    private LocalDateTime createdAt;

    public static QuestionDto fromEntity(Question question) {
        String writerId = null;
        String writerType = null;

        if (question.getUser() != null) {
            writerId = question.getUser().getUserid();
            writerType = "user";
        } else if (question.getNuser() != null) {
            writerId = question.getNuser().getNUserid();
            writerType = "nuser";
        }

        return QuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .isAnswered(question.isAnswered())
                .writer(writerId)
                .writerType(writerType)
                .createdAt(question.getCreatedAt())
                .build();
    }
}
