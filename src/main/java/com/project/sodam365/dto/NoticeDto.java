package com.project.sodam365.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
    private Long id;
    private String n_title;         // 공지 제목
    private String n_content;       // 공지 내용
    private String user;            // 작성자 (admin)
    private LocalDateTime createdAt; // 작성일

    public static NoticeDto fromEntity(com.project.sodam365.entity.Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getN_title(),
                notice.getN_content(),
                notice.getUser(),
                notice.getCreatedAt()
        );
    }

}
