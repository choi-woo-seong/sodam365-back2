package com.project.sodam365.dto;

import com.project.sodam365.entity.Answer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDto {
    private Long no;
    private String a_title;
    private String a_contents;

    public static QnaDto fromEntity(Answer answer) {
        return QnaDto.builder()
                .no(answer.getNo())
                .a_title(answer.getA_title())
                .a_contents(answer.getA_contents())
                .build();
    }
}
