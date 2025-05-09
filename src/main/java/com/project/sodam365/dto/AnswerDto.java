package com.project.sodam365.dto;

import com.project.sodam365.entity.Answer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {
    private Long id;
    private String a_title;
    private String a_contents;
    private String answer;
    private Long questionId;
    private LocalDateTime createdAt;

    public static AnswerDto fromEntity(Answer answer) {
        return AnswerDto.builder()
                .id(answer.getId())
                .a_title(answer.getA_title())
                .a_contents(answer.getA_contents())
                .answer(answer.getAnswer())
                .questionId(answer.getQuestion().getId())
                .createdAt(answer.getCreatedAt())
                .build();
    }
}

