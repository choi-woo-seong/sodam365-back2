package com.project.sodam365.dto;

import com.project.sodam365.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private String content;
    private Long questionId;
    private String adminId;

    public static AnswerDto fromEntity(Answer answer) {
        return new AnswerDto(
                answer.getId(),
                answer.getContent(),
                answer.getQuestion().getId(),
                answer.getAdminId()
        );
    }
}
